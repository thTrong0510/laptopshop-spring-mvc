<!-- Modal structure -->
<div style="display: none;" class="warning-modal-overlay" id="warning-myModal">
    <div class="warning-modal-content">
        <!-- Close modal (x) -->
        <button class="warning-close-modal" id="warning-closeModalIcon">&times;</button>

        <h2>Confirm Action</h2>
        <p>Are you sure you want to proceed?</p>

        <form id="warning-modal-form" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="warning-value" id="warning-value" />
            <!-- Yes/No buttons -->
            <div class="warning-button-container">
                <button type="submit" class="warning-modal-button yes" id="warning-yesButton">Yes</button>
                <button class="warning-modal-button no" id="warning-noButton">No</button>
            </div>
        </form>
    </div>
</div>