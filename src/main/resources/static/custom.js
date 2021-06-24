document.addEventListener('DOMContentLoaded', () => {
    let itemCounters = document.getElementsByClassName("order-item-count");
    if (itemCounters) {
        Array.prototype.forEach.call(itemCounters, (item) => {
            item.addEventListener('change', () => {
                updateOrderTotal(itemCounters);
            })
        });
        updateOrderTotal(itemCounters);
    }

    let incrementButtons = document.getElementsByClassName("increment-button");
    if (incrementButtons)
        Array.prototype.forEach.call(incrementButtons, (item) => {
            item.addEventListener('click', () => {
                let counter = item.parentNode.parentNode.getElementsByClassName('order-item-count')[0];

                counter.value = parseInt(counter.value) + 1;

                if (counter.value === "1") {
                    let decrementButton = item.parentNode.getElementsByClassName('decrement-button')[0];
                    decrementButton.disabled = false;
                }

                updateOrderTotal(itemCounters);
            })
        });

    let decrementButtons = document.getElementsByClassName("decrement-button");
    if (decrementButtons)
        Array.prototype.forEach.call(decrementButtons, (item) => {
            let counter = item.parentNode.parentNode.getElementsByClassName('order-item-count')[0];
            item.disabled = counter.value === "0";

            item.addEventListener('click', () => {
                let counter = item.parentNode.parentNode.getElementsByClassName('order-item-count')[0];

                counter.value = parseInt(counter.value) > 0 ? parseInt(counter.value) - 1 : 0;

                item.disabled = counter.value === "0";

                updateOrderTotal(itemCounters);

            })
        });

    let orderTotals = document.getElementsByClassName("item-total-price");
    if (orderTotals) {
        let sum = 0;
        for (let orderTotal of orderTotals) {
            const cost = parseInt(orderTotal.getAttribute('data-price'));
            sum += cost;
        }
        const element = document.getElementById('order-total-price');
        if (element !== null) {
            element.textContent = `€${(sum / 100).toFixed(2)}`;
        }
    }

});

function updateOrderTotal(itemCounters) {
    let sum = 0;
    for (let itemCounter of itemCounters) {
        sum += itemCounter.value * itemCounter.getAttribute('data-price')
    }
    const element = document.getElementById('orderTotal');
    if (element !== null) {
        element.textContent = (sum / 100).toString();
    }
}

