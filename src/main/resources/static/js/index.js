
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
    let files = $('#files');

    let formData = new FormData();
    let inputFile = $("input[name='files']").files;
    for (var i=0; i<inputFile.length; i++) {
        formData.append("files", inputFile[i]);
    }
    formData.append("title",title);
    formData.append("writer",writer);
    formData.append("password",password);
    formData.append("content",content);
    console.log(formData);

    let data = {'title': title, 'content': content, 'writer': writer, 'password': password};

    $.ajax({
        type: "POST",
        url: "/api/schedules",
        contentType: false,
        processData: false,
        data: formData,
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

// 파일 선택
function selectFile(element) {

    const file = element.files[0];
    const filename = element.closest('.file_input').firstElementChild;

    // 1. 파일 선택 창에서 취소 버튼이 클릭된 경우
    if ( !file ) {
        filename.value = '';
        return false;
    }

    // 2. 파일 크기가 10MB를 초과하는 경우
    const fileSize = Math.floor(file.size / 1024 / 1024);
    if (fileSize > 10) {
        alert('10MB 이하의 파일로 업로드해 주세요.');
        filename.value = '';
        element.value = '';
        return false;
    }

    // 3. 파일명 지정
    filename.value = file.name;
}

// 파일 추가
function addFile() {
    const fileDiv = document.createElement('div');
    fileDiv.innerHTML =`
            <div class="file_input">
                <input type="text" readonly />
                <label> 첨부파일
                    <input type="file" name="files" onchange="selectFile(this);" />
                </label>
            </div>
            <button type="button" onclick="removeFile(this);" class="btns del_btn"><span>삭제</span></button>
        `;
    document.querySelector('.file_list').appendChild(fileDiv);
}

// 파일 삭제
function removeFile(element) {
    const fileAddBtn = element.nextElementSibling;
    if (fileAddBtn) {
        const inputs = element.previousElementSibling.querySelectorAll('input');
        inputs.forEach(input => input.value = '')
        return false;
    }
    element.parentElement.remove();
}