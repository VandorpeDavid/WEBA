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
                let counter = item.parentNode.getElementsByClassName('order-item-count')[0];

                counter.value = parseInt(counter.value) + 1;
                updateOrderTotal(itemCounters);
            })
        });

    let decrementButtons = document.getElementsByClassName("decrement-button");
    if (decrementButtons)
        Array.prototype.forEach.call(decrementButtons, (item) => {
            item.addEventListener('click', () => {
                let counter = item.parentNode.getElementsByClassName('order-item-count')[0];

                counter.value = parseInt(counter.value) > 0 ? parseInt(counter.value) - 1 : 0;


                updateOrderTotal(itemCounters);

            })
        });
});

function updateOrderTotal(itemCounters) {
    let sum = 0;
    for (let itemCounter of itemCounters) {
        console.log(itemCounter.value)
        console.log(itemCounter.getAttribute('data-price'))
        sum += itemCounter.value * itemCounter.getAttribute('data-price')
    }
    const element = document.getElementById('orderTotal');
    if (element !== null) {
        element.textContent = (sum / 100).toString();
    }
}
