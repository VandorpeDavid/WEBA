import React, { useEffect, useCallback, useReducer } from "react";
import { OrderComponent } from "./Order";
import { CSSTransitionGroup } from 'react-transition-group';
import { useTranslation } from 'react-i18next';
import { Order } from '../models';
import { Client as StompJsClient } from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import playAlert from 'alert-sound-notify';

interface OrdersState {
    completed: Order[];
    unfinished: Order[];
    status: string;
}

const completedStates = ['COMPLETED', 'REJECTED'];
const completedOrderLogSize = 5;

function deleteOrder(orders: Order[], newOrder: Order ) {
    let index = orders.findIndex((order) => order.id === newOrder.id);

    if (index < 0) {
        return false;
    } else {
        const order = orders[index];
        orders.splice(index, 1);
        return order;
    }
}

function updateOrders(orders: Order[], newOrder: Order, insertIfMissing = true, sortOnInsert = false) {
    let index = orders.findIndex((order) => order.id === newOrder.id);

    if (index < 0) {
        if (insertIfMissing) {
            newOrder.completedByMe = false;
            orders.push(newOrder);
            if (sortOnInsert) {
                console.log("SORT", orders);
                orders.sort((o1, o2) => o1.created.getTime() - o2.created.getTime())
            }

            return true;
        } else {
            return false;
        }
    } else {
        newOrder.completedByMe ||= orders[index].completedByMe;
        orders[index] = newOrder;

        return false;
    }
}

function processIncomingData(orders: OrdersState, order: Order) {
    const unfinishedOrders = [...orders.unfinished];
    const completedOrders = [...orders.completed];
    let nextOrders: OrdersState = { ...orders};
    if (completedStates.includes(order.status)) {
        const completedOrder = deleteOrder(unfinishedOrders, order);
        if (completedOrder) {
            if (completedOrder.completedByMe) {
                updateOrders(completedOrders, order);

                while (completedOrders.length > completedOrderLogSize) {
                    completedOrders.shift();
                }

                nextOrders.completed = completedOrders;
            } else {
                updateOrders(nextOrders.completed, order, false);
                nextOrders.completed = completedOrders;
            }

            nextOrders.unfinished = unfinishedOrders;
        } else {
            updateOrders(completedOrders, order, false);
            nextOrders.completed = completedOrders;
        }
    } else {
        let newOrder = true;
        if (deleteOrder(completedOrders, order)) {
            nextOrders.completed = completedOrders;
            newOrder = false;
        }

        console.log(order.status, completedStates)
        if(updateOrders(unfinishedOrders, order, true, true) && newOrder) {
            playAlert('glass');
        }
        nextOrders.unfinished = unfinishedOrders;
    }

    return nextOrders;
}

function ordersReducer(orders, { order, type }) {
    switch(type) {
        case 'incomingData':
            return processIncomingData(orders, order);
        case 'markAsCompletedByMe':
           const newOrder = {...order};
           newOrder.completedByMe = true;
           const unfinished = [...orders.unfinished];

           updateOrders(unfinished, newOrder, false);

           return { ...orders, unfinished };
    }
}

export const BarComponent = ({ barId }) => {
    const { t } = useTranslation();
    const [orders, dispatchOrders] = useReducer(ordersReducer, { unfinished: [],  completed: []});

    useEffect(
        () => {
            console.log('effect');
            const stompClient = new StompJsClient();

            stompClient.webSocketFactory = () => {
                return new SockJS('/websockets');
            }
            
            stompClient.onConnect =  function (frame) {
                console.log('Connected: ' + frame);
                console.log("orderId", barId);
                stompClient.subscribe('/events/unfinishedOrders/' + barId, function (order) {
                    const data = JSON.parse(order.body);
                    console.log("data", data);
                    data.created = new Date(data.created);
                    console.log(data);
                    dispatchOrders({ order: data, type: 'incomingData' });
                });
            }
            
            stompClient.activate();

            return function cleanup() {
                stompClient.deactivate();
                console.log('Disconnecting');
            }
        },
        [barId]
    );

    const markAsCompletedByMe = useCallback((order) => {
        dispatchOrders({ order, type: 'markAsCompletedByMe' });
    }, []);

    return <>
        <div className="col d-flex flex-column">
            <h1>{t('orders.unfinished')}</h1>
            <CSSTransitionGroup
                transitionName="order-list"
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}>
                {
                    orders.unfinished.map((order) => <OrderComponent order={order} key={order.id}
                                                                      markAsCompletedByMe={markAsCompletedByMe}/>)
                }
            </CSSTransitionGroup>
        </div>
        <div className="col d-flex flex-column">
            <h1>{t('orders.recentlyFinishedByMe')}</h1>
            <CSSTransitionGroup
                transitionName="order-list"
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}>
                {
                    orders.completed.map((order) => <OrderComponent order={order} key={order.id}
                                                                     markAsCompletedByMe={markAsCompletedByMe}/>)
                }
            </CSSTransitionGroup>
        </div>
    </>;
};