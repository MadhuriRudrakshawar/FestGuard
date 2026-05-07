const API = {
    dashboard: "/api/dashboard/summary",
    areas: "/api/festival-areas",
    area: function (areaId) { return `/api/festival-areas/${areaId}`; },
    reports: "/api/crowd-reports/recent",
    reportForArea: function (areaId) { return `/api/crowd-reports/area/${areaId}`; },
    alerts: "/api/crowd-alerts/active",
    resolveAlert: function (alertId) { return `/api/crowd-alerts/${alertId}/resolve`; }
};

$(document).ready(function () {
    setupNavigation();
    setupForms();
    setupClock();
    setupMobileSidebar();
    loadAll();
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
    $("#heroDate").text(now.toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }));
}

function setupForms() {
    $("#areaForm").on("submit", function (e) { e.preventDefault(); createArea(); });
    $("#reportForm").on("submit", function (e) { e.preventDefault(); submitReport(); });
}

function validateInput(selector) {
    const input = $(selector);
    if (!input.val() || input.val().trim() === "") {
        input.addClass("is-invalid");
        return false;
    }
    input.removeClass("is-invalid");
    return true;
}

function createArea() {
    const valid = validateInput("#areaName") && validateInput("#areaDesc") && validateInput("#areaType");
    if (!valid) return;

    const area = {
        name: $("#areaName").val().trim(),
        description: $("#areaDesc").val().trim(),
        areaType: $("#areaType").val()
    };

    $("#areaSubmitBtn").prop("disabled", true).text("Creating...");

    $.ajax({
        url: API.areas, method: "POST", contentType: "application/json", data: JSON.stringify(area),
        success: function () {
            showSuccess("Festival area created.");
            $("#areaForm")[0].reset();
            loadAreas();
            loadDashboard();
        },
        error: function (xhr) { showError(getErrorMessage(xhr, "Could not create area.")); },
        complete: function () { $("#areaSubmitBtn").prop("disabled", false).html('<i class="bi bi-plus-circle"></i> Create Area'); }
    });
}

function loadAreas() {
    $("#areasLoading").removeClass("d-none");
    $("#areasEmpty").addClass("d-none");
    $("#areasGrid").addClass("d-none");

    $.ajax({
        url: API.areas, method: "GET",
        success: function (areas) { renderAreas(areas); populateAreaDropdown(areas); },
        error: function () { showError("Could not load festival areas."); },
        complete: function () { $("#areasLoading").addClass("d-none"); }
    });
}

function renderAreas(areas) {
    const grid = $("#areasGrid").empty();
    if (!areas || areas.length === 0) { $("#areasEmpty").removeClass("d-none"); return; }
    areas.forEach(function (area) {
        grid.append(`
            <div class="area-card">
                <div class="area-card-header">
                    <div class="area-card-name">${escapeHtml(area.name)}</div>
                    <button class="btn-icon-danger" type="button" title="Delete area" onclick="deleteArea(${area.id})">
                        <i class="bi bi-x-lg"></i>
                    </button>
                </div>
                <div class="area-card-desc">${escapeHtml(area.description || "")}</div>
                <span class="type-badge">${escapeHtml(area.areaType || "OTHER")}</span>
            </div>
        `);
    });
    grid.removeClass("d-none");
}

function deleteArea(id) {
    if (!confirm("Delete this festival area and its reports/alerts?")) return;
    $.ajax({
        url: API.area(id), method: "DELETE",
        success: function () {
            showSuccess("Festival area deleted.");
            loadAreas(); loadReports(); loadAlerts(); loadDashboard();
        },
        error: function (xhr) { showError(getErrorMessage(xhr, "Could not delete area.")); }
    });
}

function populateAreaDropdown(areas) {
    const select = $("#reportArea").empty();
    select.append(`<option value="">Select area...</option>`);
    if (!areas) return;
    areas.forEach(function (area) {
        select.append(`<option value="${area.id}">${escapeHtml(area.name)}</option>`);
    });
}

function submitReport() {
    const valid = validateInput("#reportArea") && validateInput("#crowdLevel") && validateInput("#reportNote");
    if (!valid) return;

    const report = {
        areaId: Number($("#reportArea").val()),
        crowdLevel: $("#crowdLevel").val(),
        note: $("#reportNote").val().trim()
    };

    $("#reportSubmitBtn").prop("disabled", true).text("Submitting...");

    $.ajax({
        url: API.reportForArea(report.areaId), method: "POST", contentType: "application/json", data: JSON.stringify(report),
        success: function () {
            showSuccess("Crowd report submitted.");
            $("#reportForm")[0].reset();
            loadReports(); loadAlerts(); loadDashboard();
        },
        error: function (xhr) { showError(getErrorMessage(xhr, "Could not submit report.")); },
        complete: function () { $("#reportSubmitBtn").prop("disabled", false).html('<i class="bi bi-send"></i> Submit Report'); }
    });
}

function loadReports() {
    $("#reportsLoading").removeClass("d-none");
    $("#reportsTable").addClass("d-none");
    $("#reportsEmpty").addClass("d-none");

    $.ajax({
        url: API.reports, method: "GET",
        success: function (reports) { renderReports(reports); },
        error: function () { showError("Could not load crowd reports."); },
        complete: function () { $("#reportsLoading").addClass("d-none"); }
    });
}

function renderReports(reports) {
    const body = $("#reportsBody").empty();
    if (!reports || reports.length === 0) { $("#reportsEmpty").removeClass("d-none"); return; }
    reports.slice(0, 10).forEach(function (r) { body.append(reportRow(r)); });
    $("#reportsTable").removeClass("d-none");
}

function reportRow(report) {
    const areaName = report.area ? report.area.name : "Unknown Area";
    const level = report.crowdLevel || "UNKNOWN";
    const time = formatTime(report.submittedAt);
    return `
        <tr>
            <td>${escapeHtml(areaName)}</td>
            <td><span class="badge-${escapeHtml(level)}">${escapeHtml(level)}</span></td>
            <td>${escapeHtml(report.note || "")}</td>
            <td>${time}</td>
        </tr>
    `;
}

function loadAlerts() {
    $("#alertsLoading").removeClass("d-none");
    $("#alertsEmpty").addClass("d-none");
    $("#alertsList").addClass("d-none");

    $.ajax({
        url: API.alerts, method: "GET",
        success: function (alerts) { renderAlerts(alerts); updateAlertBadge(alerts); },
        error: function () { showError("Could not load alerts."); },
        complete: function () { $("#alertsLoading").addClass("d-none"); }
    });
}

function renderAlerts(alerts) {
    const list = $("#alertsList").empty();
    if (!alerts || alerts.length === 0) { $("#alertsEmpty").removeClass("d-none"); return; }
    alerts.forEach(function (alert) { list.append(alertCard(alert)); });
    list.removeClass("d-none");
}

function alertCard(alert) {
    const areaName = alert.area ? alert.area.name : "Unknown Area";
    const message = alert.message || "Area is full.";
    const time = formatTime(alert.createdAt);
    return `
        <div class="alert-card alert-full">
            <div class="alert-card-header">
                <span class="alert-card-name">${escapeHtml(areaName)}</span>
                <i class="bi bi-exclamation-triangle-fill text-danger"></i>
            </div>
            <div class="alert-card-note">${escapeHtml(message)}</div>
            <div class="alert-card-footer">
                <span class="alert-time">${time}</span>
                <button class="btn-resolve" onclick="resolveAlert(${alert.id})">Resolve</button>
            </div>
        </div>
    `;
}

function resolveAlert(id) {
    $.ajax({
        url: API.resolveAlert(id), method: "PATCH",
        success: function () { showSuccess("Alert resolved."); loadAlerts(); loadDashboard(); },
        error: function (xhr) { showError(getErrorMessage(xhr, "Could not resolve alert.")); }
    });
}

function updateAlertBadge(alerts) {
    const count = alerts ? alerts.length : 0;
    count > 0 ? $("#alertBadge").text(count).show() : $("#alertBadge").hide();
}

let crowdLevelChart = null;
let reportsPerAreaChart = null;

function loadDashboard() {
    $.when(
        $.get(API.dashboard),
        $.get(API.reports)
    ).done(function (summaryRes, reportsRes) {
        const summary = summaryRes[0];
        const reports = reportsRes[0] || [];

        $("#m-areas").text(summary.totalAreas ?? 0);
        $("#m-reports").text(summary.totalReports ?? 0);
        $("#m-alerts").text(summary.activeAlerts ?? 0);

        renderCrowdLevelChart(reports);
        renderReportsPerAreaChart(reports);
    }).fail(function () {
        loadDashboardFallback();
    });
}

function renderCrowdLevelChart(reports) {
    const counts = { LOW: 0, MEDIUM: 0, FULL: 0 };
    reports.forEach(function (r) { counts[r.crowdLevel]++; });

    if (crowdLevelChart) crowdLevelChart.destroy();
    crowdLevelChart = new Chart(document.getElementById("crowdLevelChart"), {
        type: "doughnut",
        data: {
            labels: ["Low", "Medium", "Full"],
            datasets: [{
                data: [counts.LOW, counts.MEDIUM, counts.FULL],
                backgroundColor: ["#74A8A4", "#7F543D", "#b96b5b"],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: "bottom", labels: { font: { size: 13 } } } },
            cutout: "65%"
        }
    });
}

function renderReportsPerAreaChart(reports) {
    const counts = {};
    reports.forEach(function (r) {
        const name = r.area ? r.area.name : "Unknown";
        counts[name] = (counts[name] || 0) + 1;
    });

    if (reportsPerAreaChart) reportsPerAreaChart.destroy();
    reportsPerAreaChart = new Chart(document.getElementById("reportsPerAreaChart"), {
        type: "bar",
        data: {
            labels: Object.keys(counts),
            datasets: [{
                label: "Reports",
                data: Object.values(counts),
                backgroundColor: "#74A8A4",
                borderRadius: 6,
                borderSkipped: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, ticks: { stepSize: 1 }, grid: { color: "#DBE2DC" } },
                x: { grid: { display: false } }
            }
        }
    });
}

function loadDashboardFallback() {
    $.when($.get(API.areas), $.get(API.reports), $.get(API.alerts))
    .done(function (areasRes, reportsRes, alertsRes) {
        $("#m-areas").text((areasRes[0] || []).length);
        $("#m-reports").text((reportsRes[0] || []).length);
        $("#m-alerts").text((alertsRes[0] || []).length);
    });
}

function formatTime(value) {
    if (!value) return "—";
    const date = new Date(value);
    return isNaN(date.getTime()) ? value : date.toLocaleString();
}

function showSuccess(message) {
    $("#successMsg").text(message);
    $("#successToast").fadeIn(150);
    setTimeout(function () { $("#successToast").fadeOut(200); }, 2500);
}

function showError(message) {
    $("#errorMsg").text(message);
    $("#errorToast").fadeIn(150);
    setTimeout(function () { $("#errorToast").fadeOut(200); }, 3500);
}

function getErrorMessage(xhr, fallback) {
    if (xhr && xhr.responseJSON) return xhr.responseJSON.message || xhr.responseJSON.error || fallback;
    if (xhr && xhr.responseText) return xhr.responseText;
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
