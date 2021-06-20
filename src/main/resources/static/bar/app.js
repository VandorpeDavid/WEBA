const React = require('react');
const ReactDOM = require('react-dom');
import Bar from "./components/Bar";

const element = document.getElementById('bar-orders');
const barId = element.dataset.id;
ReactDOM.render(
    <Bar barId={barId}/>,
    element
);

