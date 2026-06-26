const loginSection = document.getElementById('loginSection');
const dashboardSection = document.getElementById('dashboardSection');
const loginForm = document.getElementById('loginForm');
const employeeForm = document.getElementById('employeeForm');
const employeeTableBody = document.querySelector('#employeeTable tbody');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const logoutBtn = document.getElementById('logoutBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const formTitle = document.getElementById('formTitle');
const submitBtn = document.getElementById('submitBtn');
const employeeMessage = document.getElementById('employeeMessage');
const loginMessage = document.getElementById('loginMessage');

let editingEmployeeId = null;

loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const payload = new URLSearchParams(new FormData(loginForm));
    const response = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: payload.toString()
    });
    const data = await response.json();
    if (data.success) {
        loginSection.classList.add('hidden');
        dashboardSection.classList.remove('hidden');
        loadEmployees();
    } else {
        loginMessage.textContent = data.message || 'Login failed';
    }
});

logoutBtn.addEventListener('click', () => {
    loginSection.classList.remove('hidden');
    dashboardSection.classList.add('hidden');
    loginForm.reset();
    loginMessage.textContent = '';
});

employeeForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const payload = new URLSearchParams(new FormData(employeeForm));
    const url = editingEmployeeId ? `/api/employees/${editingEmployeeId}` : '/api/employees';
    const method = editingEmployeeId ? 'PUT' : 'POST';

    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: payload.toString()
    });
    const data = await response.json();
    employeeMessage.textContent = data.message || 'Saved';
    if (data.success) {
        employeeForm.reset();
        editingEmployeeId = null;
        formTitle.textContent = 'Add Employee';
        submitBtn.textContent = 'Save Employee';
        cancelEditBtn.classList.add('hidden');
        loadEmployees();
    }
});

searchBtn.addEventListener('click', () => loadEmployees(searchInput.value));
searchInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        loadEmployees(searchInput.value);
    }
});

cancelEditBtn.addEventListener('click', () => {
    editingEmployeeId = null;
    employeeForm.reset();
    formTitle.textContent = 'Add Employee';
    submitBtn.textContent = 'Save Employee';
    cancelEditBtn.classList.add('hidden');
    employeeMessage.textContent = '';
});

async function loadEmployees(search = '') {
    const response = await fetch(`/api/employees?search=${encodeURIComponent(search)}`);
    const data = await response.json();
    employeeTableBody.innerHTML = '';
    if (!data.success) {
        employeeTableBody.innerHTML = `<tr><td colspan="5">${data.message || 'No records found'}</td></tr>`;
        return;
    }

    data.employees.forEach((employee) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${employee.empId}</td>
            <td>${employee.name}</td>
            <td>${employee.phone || ''}</td>
            <td>${employee.designation || ''}</td>
            <td>
                <button class="secondary edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </td>
        `;
        row.querySelector('.edit-btn').addEventListener('click', () => editEmployee(employee));
        row.querySelector('.delete-btn').addEventListener('click', () => deleteEmployee(employee.empId));
        employeeTableBody.appendChild(row);
    });
}

function editEmployee(employee) {
    editingEmployeeId = employee.empId;
    formTitle.textContent = 'Edit Employee';
    submitBtn.textContent = 'Update Employee';
    cancelEditBtn.classList.remove('hidden');

    document.getElementById('empId').value = employee.empId;
    document.getElementById('name').value = employee.name || '';
    document.getElementById('fatherName').value = employee.fatherName || '';
    document.getElementById('dob').value = employee.dob || '';
    document.getElementById('salary').value = employee.salary || '';
    document.getElementById('address').value = employee.address || '';
    document.getElementById('phone').value = employee.phone || '';
    document.getElementById('email').value = employee.email || '';
    document.getElementById('education').value = employee.education || '';
    document.getElementById('designation').value = employee.designation || '';
    document.getElementById('aadhar').value = employee.aadhar || '';
    employeeMessage.textContent = '';
}

async function deleteEmployee(empId) {
    const response = await fetch(`/api/employees/${empId}`, { method: 'DELETE' });
    const data = await response.json();
    employeeMessage.textContent = data.message || 'Deleted';
    loadEmployees(searchInput.value);
}
