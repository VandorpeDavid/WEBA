import React from "react";
import OrderActions from "./OrderActions";
import OrderItem from "./OrderItem";
import { useTranslation } from 'react-i18next';

export default ({markAsCompletedByMe, order}) => {
    const { t } = useTranslation();
    return <span className="collapsible-wrapper">
        <div className={'card todo-order collapsible react-order status-' + order.status}>
            <h3>{t('orders.table', {table: order.table})}</h3>
            <h4>{t('orders.status', {status: t('orders.states.' + order.status)})}</h4>
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
                    order.items.map((item) => <OrderItem item={item} key={item.name}/>)
                }
                </tbody>
            </table>
            <h4>{t('orders.total', {total: order.items.reduce((sum, item) => sum + (item.amount * item.pricePerItem / 100), 0)})}</h4>
            <OrderActions order={order} markAsCompletedByMe={markAsCompletedByMe}/>
        </div>
    </span>;
};