<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Dashboard - Update Product</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
                    integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
                    crossorigin="anonymous">
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    $(document).ready(() => {
                        if ($(productPreview).attr("src").trim() !== "\\images\\product") {
                            $("#productPreview").css({ "display": "block" });
                        }

                        const productFile = $("#productFile");
                        productFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#productPreview").attr("src", imgURL);
                            $("#productPreview").css({ "display": "block" });
                        });
                    });
                </script>

            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Product</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Product</li>
                                </ol>
                                <div>
                                    <div class="row">
                                        <div class="col-md-10 col-12 mx-auto">
                                            <h3>Update A Product</h3>
                                            <hr>

                                            <form:form action="/admin/product/update" method="post"
                                                modelAttribute="product" enctype="multipart/form-data">
                                                <c:set var="nameError">
                                                    <form:errors path="name" cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="priceError">
                                                    <form:errors path="price" cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="detailDescError">
                                                    <form:errors path="productDetail.detailDesc"
                                                        cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="quantityError">
                                                    <form:errors path="quantity" cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="shortDescError">
                                                    <form:errors path="productDetail.shortDesc"
                                                        cssClass="invalid-feedback" />
                                                </c:set>
                                                <div class="mb-3" style="display: none;">
                                                    <label class="form-label">Id</label>
                                                    <form:input type="text" class="form-control" path="id" />
                                                </div>
                                                <div class="d-flex justify-content-between">
                                                    <div class="mb-3 col-md-5 col-12">
                                                        <label class="form-label">Name:</label>
                                                        <form:input type="text" path="name"
                                                            class="form-control ${not empty nameError? 'is-invalid':''}" />
                                                        <form:errors path="name" cssClass="invalid-feedback" />
                                                    </div>
                                                    <div class="mb-3 col-md-5 col-12">
                                                        <label class="form-label">Price</label>
                                                        <form:input type="number" path="price"
                                                            class="form-control ${not empty priceError? 'is-invalid':''}" />
                                                        <form:errors path="price" cssClass="invalid-feedback" />
                                                    </div>
                                                </div>
                                                <div class="mb-3 col-md-12">
                                                    <label class="form-label">Detail Description</label>
                                                    <form:input type="text" path="productDetail.detailDesc"
                                                        class="form-control ${not empty detailDescError? 'is-invalid':''}" />
                                                    <form:errors path="productDetail.detailDesc"
                                                        cssClass="invalid-feedback" />
                                                </div>
                                                <div class="d-flex justify-content-between">
                                                    <div class="mb-3 col-md-5 col-12 ">
                                                        <label class="form-label">Quantity</label>
                                                        <form:input type="number" path="quantity"
                                                            class="form-control ${not empty quantityError? 'is-invalid':''}" />
                                                        <form:errors path="quantity" cssClass="invalid-feedback" />
                                                    </div>
                                                    <div class=" mb-3 col-md-5 col-12">
                                                        <label class="form-label">Short Description</label>
                                                        <form:input type="text" path="productDetail.shortDesc"
                                                            class="form-control ${not empty shortDescError? 'is-invalid':''}" />
                                                        <form:errors path="productDetail.shortDesc"
                                                            cssClass="invalid-feedback" />
                                                    </div>
                                                </div>

                                                <div class="d-flex justify-content-between">
                                                    <div class="mb-3 col-md-5 col-12">
                                                        <label class="form-label">Factory</label>
                                                        <form:select class="form-select" path="factory">
                                                            <form:option value="Apple">Apple(MacBook)</form:option>
                                                            <form:option value="Asus">Asus</form:option>
                                                            <form:option value="Lenovo">Lenovo</form:option>
                                                            <form:option value="Dell">Dell</form:option>
                                                            <form:option value="LG">LG</form:option>
                                                            <form:option value="Acer">Acer</form:option>
                                                        </form:select>
                                                    </div>
                                                    <div class="mb-3 col-md-5 col-12">
                                                        <label class="form-label">Target</label>
                                                        <form:select class="form-select" path="target">
                                                            <form:option value="Gaming">Gaming</form:option>
                                                            <form:option value="Student_Office">Student - Office
                                                            </form:option>
                                                            <form:option value="Graphic_Design">Graphic Design
                                                            </form:option>
                                                        </form:select>
                                                    </div>
                                                </div>
                                                <div class="mb-3 col-md-5 col-12">
                                                    <label for="ProductFile" class="form-label">Image</label>
                                                    <div class="input-group mb-3">
                                                        <input type="file" class="form-control" id="productFile"
                                                            accept=".png, .jpg, .jpeg" name="nameProductFile">
                                                    </div>
                                                </div>
                                                <div class="col-12 mb3 mx-auto">
                                                    <img style="max-height: 250px; display: none;" alt="product preview"
                                                        id="productPreview" class="mx-auto" src="${pathProduct}">
                                                </div>
                                                <div class="col-12 mb3">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </form:form>
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
                <script src="js/scripts.js"></script>
            </body>

            </html>