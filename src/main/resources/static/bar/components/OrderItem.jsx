import React from "react";

export default ({item}) => <tr>
    <td>{item.name}</td>
    <td>{item.amount}</td>
    <td>{item.pricePerItem / 100}</td>
    <td>{item.amount * item.pricePerItem / 100}</td>
</tr>;