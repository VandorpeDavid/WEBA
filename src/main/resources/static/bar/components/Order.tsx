import React from "react";
import { OrderActionsComponent } from "./OrderActions";
import { OrderItemComponent } from "./OrderItem";
import { useTranslation } from 'react-i18next';
import { Order as Order } from '../models';

interface OrderComponentProps {
    order: Order;
    markAsCompletedByMe: (order: Order) => void
}

export const OrderComponent : React.FunctionComponent<OrderComponentProps> = ({markAsCompletedByMe, order}) => {
    const { t } = useTranslation();
    return <span className="collapsible-wrapper">
        <div className={'card todo-order collapsible react-order status-' + order.status}>
            <h3>{t('orders.table', {table: order.table})}</h3>
            <h4>{t('orders.status', {status: t('orders.states.' + order.status)})}</h4>
            <h4>{t('orders.created', {created: order.created.toLocaleTimeString()})}</h4>
            <table className={"table table-striped table-sm"}>
                <thead>
                <tr>
                    <th scope={"col"}>{t('orders.name')}</th>
                    <th scope={"col"}>{t('orders.amount')}</th>
                    <th scope={"col"}>{t('orders.priceEach')}</th>
                    <th scope={"col"}>{t('orders.totalPrice')}</th>
                </tr>
                </thead>
                <tbody>
                {
                    order.items.map((item) => <OrderItemComponent item={item} key={item.name}/>)
                }
                </tbody>
            </table>
            <h4>{t('orders.total', {total: order.items.reduce((sum, item) => sum + (item.amount * item.pricePerItem / 100), 0)})}</h4>
            <OrderActionsComponent order={order} markAsCompletedByMe={markAsCompletedByMe}/>
        </div>
    </span>;
};