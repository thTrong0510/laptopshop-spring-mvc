window.addEventListener('DOMContentLoaded', event => {// Get references to modal elements
    const modal = document.getElementById('warning-myModal');
    const openModalButtons = document.getElementsByClassName("warning-openModalButton");
    const closeModalIcon = document.getElementById('warning-closeModalIcon');
    const yesButton = document.getElementById('warning-yesButton');
    const noButton = document.getElementById('warning-noButton');
    const warningModalForm = document.getElementById('warning-modal-form');

    if (modal) {
        // Open modal
        if (openModalButtons) {
            for (let i = 0; i < openModalButtons.length; i++) {
                openModalButtons[i].addEventListener('click', () => {
                    const userId = openModalButtons[i].getAttribute('data-user-id');
                    const productId = openModalButtons[i].getAttribute('data-product-id');
                    const orderId = openModalButtons[i].getAttribute('data-order-id');
                    if (userId) {
                        warningModalForm.action = `/admin/user/delete/${userId}`;
                    }
                    if (productId) {
                        warningModalForm.action = `/admin/product/delete/${productId}`;
                    }
                    if (orderId) {
                        warningModalForm.action = `/admin/order/delete/${orderId}`;
                    }
                    modal.style.display = 'flex'; // Show modal
                });
            }

            // Close modal (x icon)
            closeModalIcon.addEventListener('click', () => {
                modal.style.display = 'none'; // Hide modal
            });

            // Handle Yes button click
            yesButton.addEventListener('click', () => {
                modal.style.display = 'none'; // Hide modal
            });

            // Handle No button click
            noButton.addEventListener('click', () => {
                modal.style.display = 'none'; // Hide modal
            });
        }

        // Close modal when clicking outside the modal content
        window.addEventListener('click', (event) => {
            if (event.target === modal) {
                modal.style.display = 'none'; // Hide modal
            }
        });
    }
})