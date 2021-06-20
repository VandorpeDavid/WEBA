import React from "react";
import autoBind from 'react-autobind';
import Order from "./Order";
import {CSSTransitionGroup} from 'react-transition-group' // ES6
import { withTranslation } from 'react-i18next';
const completedStates = ['COMPLETED', 'REJECTED'];
const completedOrderLogSize = 5;

function deleteOrder(orders, newOrder) {
    let index = orders.findIndex((order) => order.id === newOrder.id);

    if (index < 0) {
        return false;
    } else {
        const order = orders[index];
        orders.splice(index, 1);
        return order;
    }
}

function updateOrders(orders, newOrder, insertIfMissing = true, sortOnInsert = false) {
    let index = orders.findIndex((order) => order.id === newOrder.id);

    if (index < 0) {
        if (insertIfMissing) {
            newOrder.completedByMe = false;
            orders.push(newOrder);
            if (sortOnInsert) {
                console.log("SORT", orders);
                orders.sort((o1, o2) => o1.created.getTime() - o2.created.getTime())
            }
            return newOrder;
        } else {
            return null;
        }
    } else {
        newOrder.completedByMe ||= orders[index].completedByMe;
        orders[index] = newOrder;

        return newOrder;
    }
}

class Bar extends React.PureComponent {
    constructor(props) {
        super(props);

        this.state = {unfinishedOrders: [], completedOrders: []};
        autoBind(this);
    }

    componentDidMount() {
        const socket = new SockJS('/websockets');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            const barId = this.props.barId;
            console.log("orderId", barId);
            stompClient.subscribe('/events/unfinishedOrders/' + barId, function (order) {
                const data = JSON.parse(order.body);
                console.log("data", data);
                data.created = new Date(data.created);
                console.log(data);
                this.setState((state) => {
                    const unfinishedOrders = [...state.unfinishedOrders];
                    const completedOrders = [...state.completedOrders];
                    let nextState = {};
                    if (completedStates.includes(data.status)) {
                        const completedOrder = deleteOrder(unfinishedOrders, data);
                        if (completedOrder) {
                            if (completedOrder.completedByMe) {
                                updateOrders(completedOrders, data);

                                while (completedOrders.length > completedOrderLogSize) {
                                    completedOrders.shift();
                                }

                                nextState.completedOrders = completedOrders;
                            } else {
                                if (updateOrders(completedOrders, data, false)) {
                                    nextState.completedOrders = completedOrders;
                                }
                            }

                            nextState.unfinishedOrders = unfinishedOrders;
                        } else {
                            if (updateOrders(completedOrders, data, false)) {
                                nextState.completedOrders = completedOrders;
                            }
                        }
                    } else {
                        if (deleteOrder(completedOrders, data)) {
                            nextState.completedOrders = completedOrders;
                        }

                        console.log(data.status, completedStates)
                        updateOrders(unfinishedOrders, data, true, true);
                        nextState.unfinishedOrders = unfinishedOrders;
                    }

                    return nextState;
                })
            }.bind(this));
        }.bind(this));
    }

    markAsCompletedByMe(order) {
        this.setState((state) => {
                const newOrder = {...order};
                newOrder.completedByMe = true;

                const unfinishedOrders = [...state.unfinishedOrders];

                updateOrders(unfinishedOrders, newOrder, false);

                return {unfinishedOrders};
            }
        );
    }

    render() {
        const t = this.props.t;
        return <React.Fragment>
            <h1>{t('orders.recentlyFinishedByMe')}</h1>
            <CSSTransitionGroup
                transitionName="order-list"
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}>
                {
                    this.state.completedOrders.map((order) => <Order order={order} key={order.id} completed={true}
                                                                     markAsCompletedByMe={this.markAsCompletedByMe}/>)
                }
            </CSSTransitionGroup>

            <h1>{t('orders.unfinished')}</h1>
            <CSSTransitionGroup
                transitionName="order-list"
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}>
                {
                    this.state.unfinishedOrders.map((order) => <Order order={order} key={order.id}
                                                                      markAsCompletedByMe={this.markAsCompletedByMe}/>)
                }
            </CSSTransitionGroup>
        </React.Fragment>;
    }
}

export default withTranslation()(Bar);