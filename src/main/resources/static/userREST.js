"use strict";
// этот код работает в современном режиме
console.log("userREST.js")
console.log("Лог в консоли браузера")

function showPrincipalData() {
    console.log("showPrincipalData")
    fetch("/userREST/user")
        .then(res => res.json())
        .then(user => {
            const roles = user.roles.map(role => role.name).join(', ').replace(/ROLE_/gi, '')
            $('#principalUsername').append(`<span>${user.username}</span>`)
            $('#principalRoles').append(`<span>${roles}</span>`)
            const u = `$(
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.department}</td>
                        <td>${user.salary}</td>
                        <td>${roles}</td>
                    </tr>)`;
            $('#userData').append(u)

            let temp = '';
            if ($('#v-pills-tab').length > 0) {
                const pageTabs = document.querySelector('#v-pills-tab');
                $(function () {
                    let role = ""
                    for (let i = 0; i < user.roles.length; i++) {
                        role = user.roles[i].name
                        console.log(role)
                        if (role === "ROLE_USER") {
                            console.log('show User Page Btn')
                            temp += `
                        <a id="userPage" href="/userREST" sec:authorize="(hasAnyAuthority('ADMIN', 'USER'))"
                           class="nav-link active" role="tab"
                           aria-controls="v-pills-profile" >User</a>
                       `;
                        }
                        if (role === "ROLE_ADMIN") {
                            console.log('show Admin Page Btn')
                            temp += `
                        <a id="adminPage" href="/adminREST" sec:authorize="hasAuthority('ADMIN')"
                           class="nav-link" role="tab"
                           aria-controls="v-pills-profile" >Admin</a>
                        `;
                        }
                    }
                    pageTabs.innerHTML = temp;
                })
            }

            if ($('#v-pills-tab2').length > 0) {
                const pageTabs = document.querySelector('#v-pills-tab2');
                $(function () {
                    let role = ""
                    for (let i = 0; i < user.roles.length; i++) {
                        role = user.roles[i].name
                        console.log(role)
                        if (role === "ROLE_USER") {
                            console.log('show User Page Btn')
                            temp += `
                        <a id="userPage" href="/userREST" sec:authorize="(hasAnyAuthority('ADMIN', 'USER'))"
                           class="nav-link" role="tab"
                           aria-controls="v-pills-profile" >User</a>
                       `;
                        }
                        if (role === "ROLE_ADMIN") {
                            console.log('show Admin Page Btn')
                            temp += `
                        <a id="adminPage" href="/adminREST" sec:authorize="hasAuthority('ADMIN')"
                           class="nav-link active" role="tab"
                           aria-controls="v-pills-profile" >Admin</a>
                        `;
                        }
                    }
                    pageTabs.innerHTML = temp;
                })
            }


            let t_user = {
                id: user.id,
                username: user.username,
                department: user.department,
                salary: user.salary,
                roles: roles
            }
            console.log(t_user)
            console.log(u)
        })
}

showPrincipalData()
