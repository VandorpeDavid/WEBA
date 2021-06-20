import React from "react";
import OrderActions from "./OrderActions";
import OrderItem from "./OrderItem";


export default ({markAsCompletedByMe, order}) => <span className="collapsible-wrapper">
    <div className="card todo-order collapsible">
        <h3>Tafel: {order.table}</h3>
        <h4>Status: {order.status}</h4>
        <table className={"table table-striped"}>
            <thead>
            <tr>
                <th scope={"col"}>Naam</th>
                <th scope={"col"}>Aantal</th>
                <th scope={"col"}>Prijs/stuk</th>
                <th scope={"col"}>Prijs totaal</th>
            </tr>
            </thead>
            <tbody>
            {
                order.items.map((item) => <OrderItem item={item} key={item.name}/>)
            }
            </tbody>
        </table>
        <h4>Totaal: € <span>{order.items.reduce((sum, item) => sum + (item.amount * item.pricePerItem / 100), 0)}</span></h4>
        <OrderActions order={order} markAsCompletedByMe={markAsCompletedByMe}/>
    </div>
</span>;