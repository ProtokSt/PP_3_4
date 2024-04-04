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
