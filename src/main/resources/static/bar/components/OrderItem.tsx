import React from "react";
import { OrderItem } from '../models';

interface OrderItemProps {
    item: OrderItem;
}

export const OrderItemComponent: React.FunctionComponent<OrderItemProps> = ({ item })=> <tr>
    <td>{item.name}</td>
    <td>{item.amount}</td>
    <td>{item.pricePerItem / 100}</td>
    <td>{item.amount * item.pricePerItem / 100}</td>
</tr>;