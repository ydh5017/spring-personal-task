// 사용자가 내용을 올바르게 입력하였는지 확인합니다.
function isValidContent(content) {
    if (content == '') {
        alert('내용을 입력해주세요');
        return false;
    }
    if (content.trim().length > 140) {
        alert('공백 포함 140자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function isValidTitle(title) {
    if (title == '') {
        alert('제목을 입력해주세요');
        return false;
    }
    if (title.trim().length > 20) {
        alert('공백 포함 20자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function isValidWriter(writer) {
    if (writer == '') {
        alert('담당자를 입력해주세요');
        return false;
    }
    if (writer.trim().length > 10) {
        alert('공백 포함 10자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function isValidPassword(password) {
    var passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

    if (password == '') {
        alert('비밀번호를 입력해주세요');
        return false;
    }
    if (!passwordRegex.test(password)) {
        alert('비밀번호는 최소 8자에서 16자까지, 영문자, 숫자 및 특수 문자를 포함해야 합니다.');
        return false;
    }
    return true;
}

// 수정 버튼을 눌렀을 때, 기존 작성 내용을 textarea 에 전달합니다.
// 숨길 버튼을 숨기고, 나타낼 버튼을 나타냅니다.
function editSchedule(id) {
    showEdits(id);
    let contents = $(`#${id}-contents`).text().trim();
    $(`#${id}-textarea`).val(contents);
}

function showEdits(id) {
    $(`#${id}-editarea`).show();
    $(`#${id}-submit`).show();
    $(`#${id}-delete`).show();
    $(`#${id}-editTitle`).show();
    $(`#${id}-editWriter`).show();
    $(`#${id}-editPassword`).show();

    $(`#${id}-contents`).hide();
    $(`#${id}-edit`).hide();
    $(`#${id}-title`).hide();
    $(`#${id}-writer`).hide();
}

$(document).ready(function () {
    // HTML 문서를 로드할 때마다 실행합니다.
    getSchedule();
})


function getSchedule() {

    $('#cards-box').empty();

    $.ajax({
        type: 'GET',
        url: '/api/schedules',
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let message = response[i];
                let id = message['id'];
                let title = message['title'];
                let content = message['content'];
                let writer = message['writer'];
                let modifiedAt = message['modifiedAt'];
                addHTML(id, title, content, writer, modifiedAt);
            }
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

function findByKeyword() {
    let keyword = $('#keyword').val();

    $('#cards-box').empty();

    $.ajax({
        type: 'GET',
        url: '/api/schedules/contents',
        data: {"keyword":keyword},
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let message = response[i];
                let id = message['id'];
                let title = message['title'];
                let content = message['content'];
                let writer = message['writer'];
                let modifiedAt = message['modifiedAt'];
                addHTML(id, title, content, writer, modifiedAt);
            }
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

function addHTML(id, title, content, writer, modifiedAt) {
    // 1. HTML 태그를 만듭니다.
    let tempHtml = `<div class="card">
                <!-- date/username 영역 -->
                <div class="metadata">
                    <div id="${id}-title" class="title">
                        ${title}
                    </div>
                    <input type="text" value="${title}" id="${id}-editTitle" style="display: none">
                </div>
                <div class="metadata">
                    <div id="${id}-writer" class="writer">
                        <span>담당자 : </span>${writer}
                    </div>
                    <input type="text" value="${writer}" id="${id}-editWriter" style="display: none">
                    <div class="date">
                        <span>수정 : </span>${modifiedAt}
                    </div>
                </div>
                <!-- contents 조회/수정 영역-->
                <div class="contents">
                    <div id="${id}-contents" class="text">
                        ${content}
                    </div>
                    <div id="${id}-editarea" class="edit">
                        <textarea id="${id}-textarea" class="te-edit" name="" id="" cols="30" rows="5"></textarea>
                    </div>
                </div>
                <input type="text" id="${id}-editPassword" style="display: none" placeholder="비밀번호를 입력하세요.">
                <!-- 버튼 영역-->
                <div class="footer">
                    <img id="${id}-edit" class="icon-start-edit" src="images/edit.png" alt="" onclick="editSchedule('${id}')">
                    <img id="${id}-delete" class="icon-delete" src="images/delete.png" alt="" onclick="submitDelete('${id}')">
                    <img id="${id}-submit" class="icon-end-edit" src="images/done.png" alt="" onclick="submitEdit('${id}')">
                    <img id="${id}-submitDelete" class="icon-end-edit" src="images/done.png" alt="" onclick="deleteOne('${id}')">
                </div>
            </div>`;
    // 2. #cards-box 에 HTML을 붙인다.
    $('#cards-box').append(tempHtml);
}

function writeSchedule() {
    let title = $('#title').val();
    let writer = $('#writer').val();
    let password = $('#password').val();
    let content = $('#content').val();

    if (isValidTitle(title) == false) {
        return;
    }
    if (isValidWriter(writer) == false) {
        return;
    }
    if (isValidPassword(password) == false) {
        return;
    }
    if (isValidContent(content) == false) {
        return;
    }

    let data = {'title': title, 'content': content, 'writer': writer, 'password': password};

    $.ajax({
        type: "POST",
        url: "/api/schedules",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('일정이 성공적으로 작성되었습니다.');
            window.location.reload();
        },
        error: err => {
        alert(err.responseJSON.message);
        }
    });
}

function submitEdit(id) {
    // 1. 작성 대상 메모의 username과 contents 를 확인합니다.
    let title = $(`#${id}-editTitle`).val();
    let writer = $(`#${id}-editWriter`).val();
    let password = $(`#${id}-editPassword`).val();
    let content = $(`#${id}-textarea`).val();

    if (isValidTitle(title) == false) {
        return;
    }
    if (isValidWriter(writer) == false) {
        return;
    }
    if (isValidPassword(password) == false) {
        return;
    }
    if (isValidContent(content) == false) {
        return;
    }

    // 3. 전달할 data JSON으로 만듭니다.
    let data = {'title': title, 'writer': writer, 'content': content, 'password': password};

    $.ajax({
        type: "PUT",
        url: `/api/schedules/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('일정 변경에 성공하였습니다.');
            window.location.reload();
        },error: err => {
        alert(err.responseJSON.message);
        }
    });
}

function submitDelete(id) {
    $(`#${id}-submitDelete`).show();
    $(`#${id}-editPassword`).show();

    $(`#${id}-edit`).hide();
    $(`#${id}-delete`).hide();
}

function deleteOne(id) {
    let password = $(`#${id}-editPassword`).val();
    if (isValidPassword(password) == false) {
        return;
    }
    let data = {'password': password};
    $.ajax({
        type: "DELETE",
        url: `/api/schedules/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('일정 삭제에 성공하였습니다.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}