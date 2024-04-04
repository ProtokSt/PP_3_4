"use strict";
// этот код работает в современном режиме
console.log("adminREST.js")

let roleList = [
    {id: 1, name: "ROLE_USER"},
    {id: 2, name: "ROLE_ADMIN"}
]

$(async function () {
    await getUsers();
    await getDefaultModal();
    await getNewUserForm();
    await createUser();

})

const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    showAllUsers: async () => await fetch('adminREST/users'),

    getOneUser: async (id) => await fetch(`adminREST/users/${id}`),

    updateUser: async (user) => await fetch(`adminREST/users`, {
        method: 'PUT', headers: userFetch.head,
        body: JSON.stringify(user)
    }),

    deleteUser: async (id) => await fetch(`adminREST/users/${id}`, {
        method: 'DELETE', headers: userFetch.head
    }),

    createUser: async (user) => await fetch('adminREST/users', {
        method: 'POST', headers: userFetch.head,
        body: JSON.stringify(user)
    }),
}

///////////////////
//-- users GET --//
async function getUsers() {
    console.log("getUsers")
    let temp = '';
    const table = document.querySelector('#allUsersTable tbody');
    await userFetch.showAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                temp += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.department}</td>
                    <td>${user.salary}</td>
                    <td>${user.roles.map(role => role.name).join(', ').replace(/ROLE_/gi, '')}</td>
                    <td>
                        <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-sm btn-info"
                            className data-toggle="modal" data-target="#editModal">Edit</button>
                    </td>
                    <td>
                        <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-sm btn-danger"
                            className data-toggle="modal" data-target="#deleteModal">Delete</button>
                    </td>
                </tr>
               `;
            })
            table.innerHTML = temp;

        })

    $("#allUsersTable").find('button').on('click', (event) => {
        let defaultModal = $('#defaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}

async function getDefaultModal() {
    console.log("getDefaultModal")
    $('#defaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}

////////////////////////
//-- users/{id} GET --//
//-- users PUT --//
async function editUser(modal, id) {
    console.log("editUser")
    let oneUser = await userFetch.getOneUser(id);
    let user = oneUser.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`;
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group text-center" id="formEdit">
                <div class="form-group">
                    <label for="id0" class="form-label text-center h6"><b>ID</b></label>
                    <input id="id0" value="${user.id}" placeholder="ID" type="text" class="form-control" 
                    aria-describedby="basic-addon1" disabled >
                </div>                
                <div class="form-group">
                    <label for="username0" class="col-form-label"><b>Username</b></label>
                    <input id="username0" value="${user.username}" name="username" placeholder="Username" type="text" 
                    class="form-control username" required >
                </div>
                <div class="form-group">
                    <label for="department0" class="com-form-label"><b>Department</b></label>
                    <input id="department0" value="${user.department}" placeholder="Department" type="text" 
                    class="form-control" required >
                </div>
                <div class="form-group">
                    <label for="salary0" class="com-form-label"><b>Salary</b></label>
                    <input id="salary0" value="${user.salary}" placeholder="Salary" type="number" 
                    class="form-control" required >
                </div>
                <div class="form-group">
                    <label for="password0" class="com-form-label"><b>Password</b></label>
                    <input id="password0" value="${user.password}" placeholder="Salary" type="password" 
                    class="form-control" required >
                </div>
                <div class="form-group">
                    <label for="roles0" class="com-form text-center pt-3 h6"><b>Role</b></label>
                    <select id="roles0" size="3" class="form-control select" style="max-height: 100px" 
                     multiple >
                        <option value="ROLE_USER">USER</option>
                        <option value="ROLE_ADMIN">ADMIN</option>
                    </select>
                </div>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
        // переносим имеющиеся роли пользователя на форму
        user.roles.map(role => document.forms["formEdit"].roles0.options[role.id - 1].selected = true);
    })

    $("#editButton").on('click', async () => {
        let checkedRoles = () => {
            let array = []
            let options = document.querySelector('#roles0').options
            for (let i = 0; i < options.length; i++) {
                if (options[i].selected) {
                    array.push(roleList[i])
                }
            }
            return array;
        }
        let id = modal.find("#id0").val().trim();
        let username = modal.find("#username0").val().trim();
        let department = modal.find("#department0").val().trim();
        let salary = modal.find("#salary0").val().trim();
        let password = modal.find("#password0").val().trim();
        let data = {
            id: id,
            username: username,
            department: department,
            salary: salary,
            password: password,
            roles: checkedRoles()
        }
        const response = await userFetch.updateUser(data);

        if (response.ok) {
            await getUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="messageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

////////////////////////
//-- users/{id} GET --//
//-- users DELETE --//
async function deleteUser(modal, id) {
    console.log("deleteUser")
    let oneUser = await userFetch.getOneUser(id);
    let user = oneUser.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group text-center" id="formDelete">
                <div class="form-group">
                    <label for="id0" class="form-label text-center h6"><b>ID</b></label>
                    <input id="id0" value="${user.id}" placeholder="ID" type="text" class="form-control" 
                    aria-describedby="basic-addon1" readonly >
                </div>                
                <div class="form-group">
                    <label for="username0" class="col-form-label"><b>Username</b></label>
                    <input id="username0" value="${user.username}" name="username" placeholder="Username" type="text" 
                    class="form-control username" readonly >
                </div>
                <div class="form-group">
                    <label for="department0" class="com-form-label"><b>Department</b></label>
                    <input id="department0" value="${user.department}" placeholder="Department" type="text" 
                    class="form-control" readonly >
                </div>
                <div class="form-group">
                    <label for="salary0" class="com-form-label"><b>Salary</b></label>
                    <input id="salary0" value="${user.salary}" placeholder="Salary" type="number" 
                    class="form-control" readonly >
                </div>
                <div class="form-group">
                    <label for="roles0" class="com-form text-center pt-3 h6"><b>Role</b></label>
                    <select id="roles0" size="3" class="form-control select" style="max-height: 100px" 
                     multiple disabled>
                        <option value="ROLE_USER">USER</option>
                        <option value="ROLE_ADMIN">ADMIN</option>
                    </select>
                </div>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
        // переносим имеющиеся роли пользователя на форму
        user.roles.map(role => document.forms["formDelete"].roles0.options[role.id - 1].selected = true);
    })

    $("#deleteButton").on('click', async () => {
        const response = await userFetch.deleteUser(id);

        if (response.ok) {
            await getUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="messageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

////////////////////////
// TAB ADD USER
async function getNewUserForm() {
    let button = $(`#new-user-tab`);
    let form = $(`#formAddNewUser`)
    button.on('click', () => {
        form.show()
    })
}

////////////////////////
//-- users POST --//
async function createUser() {
    $('#btnAddUser').click(async () =>  {
        let addUserForm = $('#formAddNewUser')
        let username = addUserForm.find("#username1").val().trim();
        let department = addUserForm.find("#department1").val().trim();
        let salary = addUserForm.find("#salary1").val().trim();
        let password = addUserForm.find("#password1").val().trim();

        let checkedRoles = () => {
            let array = []
            let options = document.querySelector('#roles1').options
            for (let i = 0; i < options.length; i++) {
                if (options[i].selected) {
                    array.push(roleList[i])
                }
            }
            return array;
        }
        let data = {
            username: username,
            department: department,
            salary: salary,
            password: password,
            roles: checkedRoles()
        }

        const response = await userFetch.createUser(data);
        if (response.ok) {
            await getUsers();
            addUserForm.find('#username1').val('');
            addUserForm.find('#department1').val('');
            addUserForm.find('#salary1').val('');
            addUserForm.find('#password1').val('');
            // снятие выделения опций с формы
            let optionsOff = document.querySelector('#roles1').options
            for (let i = 0; i < optionsOff.length; i++) {
                optionsOff[i].selected = false;
            }
            let alert = `<div class="alert alert-success alert-dismissible fade show col-12" role="alert" id="successMessage">
                         Пользователь добавлен в базу данных
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert);
            $('.nav-tabs a[href="#tabAllUsers"]').tab('show');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="messageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert);
        }
    });
}