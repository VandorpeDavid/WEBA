import React from "react";
import axios from "axios";
import qs from 'querystring';
import autoBind from 'react-autobind';
import { IoIosCheckmark, IoIosClose, IoIosPlay, IoMdRefresh } from "react-icons/io";
const csrfToken = $("meta[name='_csrf']").attr("content");

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

class CompleteOrderAction extends React.PureComponent {
    constructor(props) {
        super(props);
        this.state = {confirmation: false}
        autoBind(this);
    }


    completeOrder() {
        if (this.props.order.status === 'ORDERED') {
            this.setState({
                confirmation: true
            });
        } else {
            this.confirmCompleteOrder(this.props.order);
        }
    }

    confirmCompleteOrder() {
        this.props.completeOrder();
    }

    cancelConfirmation() {
        this.setState({
            confirmation: false
        });
    }

    render() {
        if (this.state.confirmation) {
            return <div>
                Ben je zeker?
                <button className={"btn btn-success"} onClick={this.confirmCompleteOrder}>Ja</button>
                <button className={"btn btn-danger"} onClick={this.cancelConfirmation}>Nee</button>
            </div>
        } else {
            return <button className={"btn btn-success"} onClick={this.completeOrder}>Afgewerkt ✓</button>
        }
    }
}

class OrderActions extends React.PureComponent {
    constructor(props) {
        super(props);
        this.state = {confirmation: false}
        autoBind(this);
    }

    cancelOrder() {
        const order = this.props.order;
        doPostRequest("/orders/cancel?order=" + order.id);
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
        return <div className={"btn-group btn-group-lg"} role={"group"}>
            <button className={"btn btn-success"} onClick={this.completeOrder}>Afgewerkt <IoIosCheckmark/></button>
            <button className={"btn btn-danger"} onClick={this.cancelOrder}>Weigeren <IoIosClose/></button>
            <button className={"btn btn-primary"} onClick={this.startOrder}>️Beginnen <IoIosPlay/></button>
            <button className={"btn btn-info"} onClick={this.resetOrder}>Reset <IoMdRefresh/></button>
        </div>;
    }
}

export default OrderActions;