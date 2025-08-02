<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Dashboard - Manage Order</title>
                    <link href="/css/styles.css" rel="stylesheet" />
                    <link href="/css/modal_warning.css" rel="stylesheet" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
                        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
                        crossorigin="anonymous">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <div class="container-fluid px-2">
                                    <h1 class="mt-4">Manage Order</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item active">Order</li>
                                    </ol>
                                    <div class="mt-5">
                                        <div class="row">
                                            <div class="col-md-10 col-12 mx-auto">
                                                <div class="d-flex justify-content-between">
                                                    <h3>Table Order</h3>
                                                </div>
                                                <hr>
                                                <table class="table text-center table-bordered table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">ID</th>
                                                            <th scope="col">Total Price</th>
                                                            <th scope="col">User</th>
                                                            <th scope="col">Status</th>
                                                            <th scope="col">Payment</th>
                                                            <th scope="col">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <c:if test="${!empty orders}">
                                                        <tbody>
                                                            <c:forEach var="order" items="${orders}" varStatus="status">
                                                                <tr>
                                                                    <th scope="row">${order.id}</th>
                                                                    <td>
                                                                        <fmt:formatNumber type="number"
                                                                            value="${order.totalPrice}" /> đ
                                                                    </td>
                                                                    <td>${users[status.index].fullName}</td>
                                                                    <td>${order.status}</td>
                                                                    <td class="text-start">
                                                                        <div>Status: ${order.paymentStatus}</div>
                                                                        <div>Ref: ${order.paymentRef}</div>
                                                                        <div>Method: ${order.paymentMethod}</div>
                                                                    </td>
                                                                    <td>
                                                                        <a href="/admin/order/${order.id}"
                                                                            class="btn btn-success">View</a>
                                                                        <a href="/admin/order/update/${order.id}"
                                                                            class="btn btn-warning mx-2 my-1">Update</a>
                                                                        <button
                                                                            class="btn btn-danger warning-openModalButton"
                                                                            data-order-id="${order.id}">Delete</button>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </c:if>
                                                </table>
                                                <nav aria-label="Page navigation example">
                                                    <ul class="pagination justify-content-center">
                                                        <li class="page-item">
                                                            <a class="${currentPage eq 1 ? 'disabled page-link' : 'page-link'}"
                                                                href="/admin/order?page=${currentPage-1}"
                                                                aria-label="Previous">
                                                                <span aria-hidden="true">&laquo;</span>
                                                            </a>
                                                        </li>
                                                        <c:forEach begin="0" end="${totalPages - 1}" varStatus="status">
                                                            <li class="page-item"><a
                                                                    class="${currentPage eq status.index + 1 ? 'active page-link' : 'page-link'}"
                                                                    href="/admin/order?page=${status.index + 1}">${status.index
                                                                    + 1}</a></li>

                                                        </c:forEach>
                                                        <li class="page-item">
                                                            <a class="${currentPage eq totalPages ? 'disabled page-link' : 'page-link'}"
                                                                href="/admin/order?page=${currentPage+1}"
                                                                aria-label="Next">
                                                                <span aria-hidden="true">&raquo;</span>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </nav>
                                                <jsp:include page="../layout/modal_warning.jsp" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </main>
                            <jsp:include page="../layout/footer.jsp" />
                        </div>
                    </div>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                        crossorigin="anonymous"></script>
                    <script src="/js/scripts.js"></script>
                    <script src="/js/js_modal_warning.js"></script>
                </body>

                </html>