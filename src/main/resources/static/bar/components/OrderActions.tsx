import React, { useCallback } from "react";
import axios from "axios";
import { stringify } from 'querystring';
import { IoIosCheckmark, IoIosClose, IoIosPlay, IoMdRefresh } from "react-icons/io";
const csrfToken = $("meta[name='_csrf']").attr("content");
import { useTranslation } from 'react-i18next';
import { Order } from '../models';

interface OrderActionsProps {
    order: Order;
    markAsCompletedByMe: (order: Order) => void
}

function doPostRequest(url) {
    axios.post(
        url,
    stringify({
            _csrf: csrfToken
        }),
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    );
}

export const OrderActionsComponent : React.FunctionComponent<OrderActionsProps> = ({ order, markAsCompletedByMe }) => {
    const { t } = useTranslation();

    const rejectOrder = useCallback(() => {
        doPostRequest("/orders/reject?order=" + order.id);
        markAsCompletedByMe(order);
    }, [doPostRequest, markAsCompletedByMe, order]);

    const completeOrder = useCallback(() => {
        doPostRequest("/orders/complete?order=" + order.id);
        markAsCompletedByMe(order);
    }, [doPostRequest, markAsCompletedByMe, order]);

    const startOrder = useCallback(() => {
        doPostRequest("/orders/start?order=" + order.id);
    }, [doPostRequest, order]);

    const resetOrder = useCallback(() => {
        doPostRequest("/orders/reset?order=" + order.id);
    }, [doPostRequest, order]);

    return <div className={"btn-group btn-group-lg"} role={"group"}>
        <button className={"btn btn-" + (order.status === 'COMPLETED' ? 'secondary disabled' : 'success')} onClick={completeOrder}>{t('orders.actions.complete')} <IoIosCheckmark/></button>
        <button className={"btn btn-" + (order.status === 'REJECTED' ? 'secondary disabled' : 'danger')} onClick={rejectOrder}>{t('orders.actions.reject')} <IoIosClose/></button>
        <button className={"btn btn-" + (order.status === 'STARTED' ? 'secondary disabled' : 'primary')} onClick={startOrder}>{t('orders.actions.start')} <IoIosPlay/></button>
        <button className={"btn btn-" + (order.status === 'ORDERED' ? 'secondary disabled' : 'info')} onClick={resetOrder}>{t('orders.actions.reset')} <IoMdRefresh/></button>
    </div>;
};
