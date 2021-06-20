import React from "react";
import axios from "axios";
import qs from 'querystring';
import autoBind from 'react-autobind';
import { IoIosCheckmark, IoIosClose, IoIosPlay, IoMdRefresh } from "react-icons/io";
const csrfToken = $("meta[name='_csrf']").attr("content");
import { withTranslation } from 'react-i18next';

function doPostRequest(url) {
    axios.post(
        url,
        qs.stringify({
            _csrf: csrfToken
        }),
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    );
}

class OrderActions extends React.PureComponent {
    constructor(props) {
        super(props);
        this.state = {confirmation: false}
        autoBind(this);
    }

    rejectOrder() {
        const order = this.props.order;
        doPostRequest("/orders/reject?order=" + order.id);
        this.props.markAsCompletedByMe(order);
    }

    completeOrder() {
        const order = this.props.order;
        doPostRequest("/orders/complete?order=" + order.id);
        this.props.markAsCompletedByMe(order);
    }

    startOrder() {
        const order = this.props.order;
        doPostRequest("/orders/start?order=" + order.id);
    }

    resetOrder() {
        const order = this.props.order;
        doPostRequest("/orders/reset?order=" + order.id);
    }

    render() {
        const t = this.props.t;
        return <div className={"btn-group btn-group-lg"} role={"group"}>
            <button className={"btn btn-success"} onClick={this.completeOrder}>{t('orders.actions.complete')} <IoIosCheckmark/></button>
            <button className={"btn btn-danger"} onClick={this.rejectOrder}>{t('orders.actions.reject')} <IoIosClose/></button>
            <button className={"btn btn-primary"} onClick={this.startOrder}>{t('orders.actions.start')} <IoIosPlay/></button>
            <button className={"btn btn-info"} onClick={this.resetOrder}>{t('orders.actions.reset')} <IoMdRefresh/></button>
        </div>;
    }
}

export default withTranslation()(OrderActions);