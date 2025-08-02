<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Place Order - LaptopShop</title>
                    <meta content="width=device-width, initial-scale=1.0" name="viewport">
                    <meta content="" name="keywords">
                    <meta content="" name="description">

                    <!-- Google Web Fonts -->
                    <link rel="preconnect" href="https://fonts.googleapis.com">
                    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                    <link
                        href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                        rel="stylesheet">

                    <!-- Icon Font Stylesheet -->
                    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
                        rel="stylesheet">

                    <!-- Libraries Stylesheet -->
                    <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
                    <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


                    <!-- Customized Bootstrap Stylesheet -->
                    <link href="/client/css/bootstrap.min.css" rel="stylesheet">

                    <!-- Template Stylesheet -->
                    <link href="/client/css/style.css" rel="stylesheet">
                </head>

                <body>
                    <jsp:include page="../layout/spinner.jsp" />
                    <jsp:include page="../layout/header.jsp" />
                    <div class="container-fluid">
                        <div class="container py-5">
                            <div class="table-responsive mb-5">
                                <div>
                                    <nav aria-label="breadcrumb">
                                        <ol class="breadcrumb">
                                            <li class="breadcrumb-item"><a href="/">Home</a></li>
                                            <li class="breadcrumb-item active" aria-current="page">Cart Details</li>
                                        </ol>
                                    </nav>
                                </div>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Products</th>
                                            <th scope="col">Name</th>
                                            <th scope="col">Price</th>
                                            <th scope="col">Quantity</th>
                                            <th scope="col">Total</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cartDetail" items="${cartDetails}" varStatus="status">
                                            <tr>
                                                <th scope="row">
                                                    <div class="d-flex align-items-center">
                                                        <img src="/images/product/${cartDetail.getProduct().image}"
                                                            class="img-fluid me-5 rounded-circle"
                                                            style="width: 80px; height: 80px;" alt="">
                                                    </div>
                                                </th>
                                                <td>
                                                    <p class="mb-0 mt-4"><a
                                                            href="/product/${cartDetail.getProduct().getId()}"
                                                            target="_blank">${cartDetail.getProduct().name}</a></p>
                                                </td>
                                                <td>
                                                    <p class="mb-0 mt-4">
                                                        <fmt:formatNumber type="number"
                                                            value="${cartDetail.getProduct().price}" /> đ
                                                    </p>
                                                </td>
                                                <td>
                                                    <div class="input-group quantity mt-4" style="width: 100px;">
                                                        <input type="text"
                                                            class="form-control form-control-sm text-center border-0"
                                                            value="${cartDetail.quantity}"
                                                            data-cart-detail-id="${cartDetail.getId()}"
                                                            data-cart-detail-price="${cartDetail.getPrice()}" disabled
                                                            style="background-color: white;">
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="mb-0 mt-4" data-cart-detail-id="${cartDetail.getId()}">
                                                        <fmt:formatNumber type="number" value="${cartDetail.price}" /> đ
                                                    </p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- Checkout Page Start -->
                    <div class="container-fluid">
                        <div class="container py-5">
                            <h1 class="mb-4">Billing details</h1>
                            <div class="row g-5">
                                <div class="col-md-12 col-lg-6 col-xl-6">
                                    <form:form action="/place-order" method="post" modelAttribute="orderDTO">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <div class="form-item">
                                            <label class="form-label my-3">Full
                                                Name<sup>*</sup></label>
                                            <form:input type="text" path="recieverName" class="form-control" />
                                        </div>
                                        <div class="form-item">
                                            <label class="form-label my-3">Mobile<sup>*</sup></label>
                                            <form:input type="text" path="recieverMobile" class="form-control" />
                                        </div>
                                        <div class="form-item">
                                            <label class="form-label my-3">Address <sup>*</sup></label>
                                            <form:input type="text" path="recieverAddress" class="form-control"
                                                placeholder="House Number Street Name" />
                                        </div>
                                        <div class="form-item">
                                            <label class="form-label my-3">Oreder Notes (Optional)</label>
                                            <textarea name="text" class="form-control" spellcheck="false" cols="30"
                                                rows="5"></textarea>
                                        </div>
                                        <div class="col-12 form-group mb-3">
                                            <label>Hình thức thanh toán</label>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="paymentMethod"
                                                    value="COD" id="COD" path="paymentMethod" checked>
                                                <label class="form-check-label" for="COD">
                                                    Thanh toán khi nhận hàng
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" path="paymentMethod" type="radio"
                                                    name="paymentMethod" value="BANKING" id="BANKING">
                                                <label class="form-check-label" for="BANKING">
                                                    Thanh toán bằng ví VNPAY
                                                </label>
                                            </div>
                                            <input style="display: none;" value="${total_price}" name="totalPrice">
                                        </div>
                                        <hr>
                                        <button type="submit"
                                            class="btn border-secondary rounded-pill px-4 py-3 text-primary text-uppercase mb-4 ms-4"
                                            type="button">Proceed Checkout</button>
                                    </form:form>
                                </div>
                                <div class="col-md-12 col-lg-6 col-xl-6">
                                    <div class="row g-4 justify-content-end">
                                        <div class="col-10">
                                            <div class="bg-light rounded">
                                                <div class="p-4">
                                                    <h1 class="display-6 mb-4">Cart <span class="fw-normal">Total</span>
                                                    </h1>
                                                    <div class="d-flex justify-content-between mb-4">
                                                        <h5 class="mb-0 me-4">Subtotal:</h5>
                                                        <p class="mb-0" data-cart-total-price="${total_price}">
                                                            <fmt:formatNumber type="number" value="${total_price}" /> đ
                                                        </p>
                                                    </div>
                                                    <div class="d-flex justify-content-between">
                                                        <h5 class="mb-0 me-4">Shipping</h5>
                                                        <div class="">
                                                            <p class="mb-0">
                                                                <fmt:formatNumber type="number" value="0" /> đ
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div
                                                    class="py-4 mb-4 border-top border-bottom d-flex justify-content-between">
                                                    <h5 class="mb-0 ps-4 me-4">Total</h5>
                                                    <p class="mb-0 pe-4" data-cart-total-price="${total_price}">
                                                        <fmt:formatNumber type="number" value="${total_price}" /> đ
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Checkout Page End -->

                    <jsp:include page="../layout/footer.jsp" />

                    <!-- Back to Top -->
                    <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i
                            class="fa fa-arrow-up"></i></a>


                    <!-- JavaScript Libraries -->
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="/client/lib/easing/easing.min.js"></script>
                    <script src="/client/lib/waypoints/waypoints.min.js"></script>
                    <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
                    <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

                    <!-- Template Javascript -->
                    <script src="/client/js/main.js"></script>
                </body>

                </html>