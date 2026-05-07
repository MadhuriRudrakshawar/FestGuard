const API = {
    dashboard: "/api/dashboard/summary",
    areas: "/api/festival-areas",
    reports: "/api/crowd-reports/recent",
    reportForArea: function (areaId) {
        return `/api/crowd-reports/area/${areaId}`;
    },
    alerts: "/api/crowd-alerts/active",
    resolveAlert: function (alertId) {
        return `/api/crowd-alerts/${alertId}/resolve`;
    }
};

$(document).ready(function () {
    setupNavigation();
    setupForms();
    setupClock();
    setupMobileSidebar();

    loadAll();

    setInterval(function () {
        loadDashboard();
        loadReports();
        loadAlerts();
    }, 10000);
});

function loadAll() {
    loadAreas();
    loadReports();
    loadAlerts();
    loadDashboard();
}

function setupNavigation() {
    $(".sidebar-nav .nav-link").on("click", function (e) {
        e.preventDefault();

        $(".sidebar-nav .nav-link").removeClass("active");
        $(this).addClass("active");

        const section = $(this).data("section");

        $(".page-section").removeClass("active");
        $("#section-" + section).addClass("active");

        $("#pageTitle").text($(this).text().trim());

        $("#sidebar").removeClass("open");
    });
}

function setupMobileSidebar() {
    $("#sidebarToggle").on("click", function () {
        $("#sidebar").toggleClass("open");
    });
}

function setupClock() {
    updateClock();
    setInterval(updateClock, 1000);
}

function updateClock() {
    const now = new Date();
    $("#clock").text(now.toLocaleTimeString());
}

function setupForms() {
    $("#areaForm").on("submit", function (e) {
        e.preventDefault();
        createArea();
    });

    $("#reportForm").on("submit", function (e) {
        e.preventDefault();
        submitReport();
    });
}

function validateInput(selector) {
    const input = $(selector);
    const value = input.val();

    if (!value || value.trim() === "") {
        input.addClass("is-invalid");
        return false;
    }

    input.removeClass("is-invalid");
    return true;
}

function createArea() {
    const valid =
        validateInput("#areaName") &&
        validateInput("#areaDesc") &&
        validateInput("#areaType");

    if (!valid) return;

    const area = {
        name: $("#areaName").val().trim(),
        description: $("#areaDesc").val().trim(),
        areaType: $("#areaType").val()
    };

    $("#areaSubmitBtn").prop("disabled", true).text("Creating...");

    $.ajax({
        url: API.areas,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(area),
        success: function () {
            showSuccess("Festival area created.");
            $("#areaForm")[0].reset();
            loadAreas();
            loadDashboard();
        },
        error: function (xhr) {
            showError(getErrorMessage(xhr, "Could not create area."));
        },
        complete: function () {
            $("#areaSubmitBtn").prop("disabled", false).html('<i class="bi bi-plus-circle"></i> Create Area');
        }
    });
}

function loadAreas() {
    $("#areasLoading").removeClass("d-none");
    $("#areasEmpty").addClass("d-none");
    $("#areasGrid").addClass("d-none");

    $.ajax({
        url: API.areas,
        method: "GET",
        success: function (areas) {
            renderAreas(areas);
            populateAreaDropdown(areas);
        },
        error: function () {
            showError("Could not load festival areas.");
        },
        complete: function () {
            $("#areasLoading").addClass("d-none");
        }
    });
}

function renderAreas(areas) {
    const grid = $("#areasGrid");
    grid.empty();

    if (!areas || areas.length === 0) {
        $("#areasEmpty").removeClass("d-none");
        return;
    }

    areas.forEach(function (area) {
        grid.append(`
            <div class="area-card">
                <div class="area-card-name">${escapeHtml(area.name)}</div>
                <div class="area-card-desc">${escapeHtml(area.description || area.location || "")}</div>
                <span class="type-badge">${escapeHtml(area.areaType || area.type || "OTHER")}</span>
            </div>
        `);
    });

    grid.removeClass("d-none");
}

function populateAreaDropdown(areas) {
    const select = $("#reportArea");
    select.empty();
    select.append(`<option value="">Select area...</option>`);

    if (!areas) return;

    areas.forEach(function (area) {
        select.append(`<option value="${area.id}">${escapeHtml(area.name)}</option>`);
    });
}

function submitReport() {
    const valid =
        validateInput("#reportArea") &&
        validateInput("#crowdLevel") &&
        validateInput("#reportNote");

    if (!valid) return;

    const report = {
        areaId: Number($("#reportArea").val()),
        crowdLevel: $("#crowdLevel").val(),
        note: $("#reportNote").val().trim()
    };

    $("#reportSubmitBtn").prop("disabled", true).text("Submitting...");

    $.ajax({
        url: API.reportForArea(report.areaId),
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(report),
        success: function () {
            showSuccess("Crowd report submitted.");
            $("#reportForm")[0].reset();
            loadReports();
            loadAlerts();
            loadDashboard();
        },
        error: function (xhr) {
            showError(getErrorMessage(xhr, "Could not submit report."));
        },
        complete: function () {
            $("#reportSubmitBtn").prop("disabled", false).html('<i class="bi bi-send"></i> Submit Report');
        }
    });
}

function loadReports() {
    setReportsLoading(true);

    $.ajax({
        url: API.reports,
        method: "GET",
        success: function (reports) {
            renderReports(reports);
        },
        error: function () {
            showError("Could not load crowd reports.");
        },
        complete: function () {
            setReportsLoading(false);
        }
    });
}

function setReportsLoading(isLoading) {
    if (isLoading) {
        $("#reportsLoading, #dashReportsLoading").removeClass("d-none");
        $("#reportsTable, #dashReportsTable").addClass("d-none");
        $("#reportsEmpty, #dashReportsEmpty").addClass("d-none");
    } else {
        $("#reportsLoading, #dashReportsLoading").addClass("d-none");
    }
}

function renderReports(reports) {
    const reportsBody = $("#reportsBody");
    const dashReportsBody = $("#dashReportsBody");

    reportsBody.empty();
    dashReportsBody.empty();

    if (!reports || reports.length === 0) {
        $("#reportsEmpty, #dashReportsEmpty").removeClass("d-none");
        return;
    }

    reports.slice(0, 10).forEach(function (report) {
        const row = reportRow(report);
        reportsBody.append(row);
        dashReportsBody.append(row);
    });

    $("#reportsTable, #dashReportsTable").removeClass("d-none");
}

function reportRow(report) {
    const areaName =
        report.areaName ||
        report.festivalAreaName ||
        (report.area ? report.area.name : "Unknown Area");

    const level = report.crowdLevel || report.level || "UNKNOWN";
    const note = report.note || "";
    const time = formatTime(report.timeSubmitted || report.submittedAt || report.createdAt);

    return `
        <tr>
            <td>${escapeHtml(areaName)}</td>
            <td><span class="badge-${escapeHtml(level)}">${escapeHtml(level)}</span></td>
            <td>${escapeHtml(note)}</td>
            <td>${time}</td>
        </tr>
    `;
}

function loadAlerts() {
    $("#alertsLoading, #dashAlertsLoading").removeClass("d-none");
    $("#alertsEmpty, #dashAlertsEmpty").addClass("d-none");
    $("#alertsList, #dashAlertsList").addClass("d-none");

    $.ajax({
        url: API.alerts,
        method: "GET",
        success: function (alerts) {
            renderAlerts(alerts);
            updateAlertBadge(alerts);
        },
        error: function () {
            showError("Could not load alerts.");
        },
        complete: function () {
            $("#alertsLoading, #dashAlertsLoading").addClass("d-none");
        }
    });
}

function renderAlerts(alerts) {
    const alertsList = $("#alertsList");
    const dashAlertsList = $("#dashAlertsList");

    alertsList.empty();
    dashAlertsList.empty();

    if (!alerts || alerts.length === 0) {
        $("#alertsEmpty, #dashAlertsEmpty").removeClass("d-none");
        return;
    }

    alerts.forEach(function (alert) {
        const card = alertCard(alert);
        alertsList.append(card);
        dashAlertsList.append(card);
    });

    alertsList.removeClass("d-none");
    dashAlertsList.removeClass("d-none");
}

function alertCard(alert) {
    const areaName =
        alert.areaName ||
        alert.festivalAreaName ||
        (alert.area ? alert.area.name : "Unknown Area");

    const message = alert.alertMessage || alert.message || "Area is full.";
    const time = formatTime(alert.timeCreated || alert.createdAt);

    return `
        <div class="alert-card alert-full">
            <div class="alert-card-header">
                <span class="alert-card-name">${escapeHtml(areaName)}</span>
                <i class="bi bi-exclamation-triangle-fill text-danger"></i>
            </div>
            <div class="alert-card-note">${escapeHtml(message)}</div>
            <div class="alert-card-footer">
                <span class="alert-time">${time}</span>
                <button class="btn-resolve" onclick="resolveAlert(${alert.id})">
                    Resolve
                </button>
            </div>
        </div>
    `;
}

function resolveAlert(id) {
    $.ajax({
        url: API.resolveAlert(id),
        method: "PATCH",
        success: function () {
            showSuccess("Alert resolved.");
            loadAlerts();
            loadDashboard();
        },
        error: function (xhr) {
            showError(getErrorMessage(xhr, "Could not resolve alert."));
        }
    });
}

function updateAlertBadge(alerts) {
    const count = alerts ? alerts.length : 0;

    if (count > 0) {
        $("#alertBadge").text(count).show();
    } else {
        $("#alertBadge").hide();
    }
}

function loadDashboard() {
    $.ajax({
        url: API.dashboard,
        method: "GET",
        success: function (summary) {
            $("#m-areas").text(summary.totalAreas ?? 0);
            $("#m-reports").text(summary.totalReports ?? 0);
            $("#m-alerts").text(summary.activeAlerts ?? 0);
            $("#m-full").text(summary.fullAreas ?? 0);
        },
        error: function () {
            // fallback if /api/dashboard/summary is not ready yet
            loadDashboardFallback();
        }
    });
}

function loadDashboardFallback() {
    $.when(
        $.get(API.areas),
        $.get(API.reports),
        $.get(API.alerts)
    ).done(function (areasRes, reportsRes, alertsRes) {
        const areas = areasRes[0] || [];
        const reports = reportsRes[0] || [];
        const alerts = alertsRes[0] || [];

        const fullAreas = new Set();

        reports.forEach(function (r) {
            if ((r.crowdLevel || r.level) === "FULL") {
                fullAreas.add(r.areaId || r.festivalAreaId || (r.area ? r.area.id : ""));
            }
        });

        $("#m-areas").text(areas.length);
        $("#m-reports").text(reports.length);
        $("#m-alerts").text(alerts.length);
        $("#m-full").text(fullAreas.size);
    });
}

function formatTime(value) {
    if (!value) return "—";

    const date = new Date(value);

    if (isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleString();
}

function showSuccess(message) {
    $("#successMsg").text(message);
    $("#successToast").fadeIn(150);

    setTimeout(function () {
        $("#successToast").fadeOut(200);
    }, 2500);
}

function showError(message) {
    $("#errorMsg").text(message);
    $("#errorToast").fadeIn(150);

    setTimeout(function () {
        $("#errorToast").fadeOut(200);
    }, 3500);
}

function getErrorMessage(xhr, fallback) {
    if (xhr && xhr.responseJSON) {
        return xhr.responseJSON.message || xhr.responseJSON.error || fallback;
    }

    if (xhr && xhr.responseText) {
        return xhr.responseText;
    }

    return fallback;
}

function escapeHtml(value) {
    if (value === null || value === undefined) return "";

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
