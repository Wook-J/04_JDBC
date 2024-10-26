// 할 일 상세 조회 페이지에서 쿼리스트링 값 얻어오기
// url 에서 얻어오면 된다!!(쿼리스트링 부분 ?todoNo=4)

// location.search : 쿼리스트링만 얻어오기!
// URLSearchParams : 쿼리스트링을 객체 형태(K=V 형태)로 다룰 수 있는 객체
const todoNo = new URLSearchParams(location.search).get("todoNo");
// ?todoNo=4 -> (todoNo[key] = 4[value])

// console.log(todoNo);   // 위의 경우 4로 나옴!

// 목록으로 버튼 클릭 시, 메인페이지("/") 요청(GET 방식)
const goToList = document.querySelector("#goToList");
goToList.addEventListener("click", () => {
  location.href = "/";
});

// 완료 여부 변경 버튼 클릭, 시 O(true) <-> X(false) 변경 요청(GET 방식)
const completeBtn = document.querySelector("#completeBtn");
completeBtn.addEventListener("click", () => {
  location.href = "/todo/complete?todoNo=" + todoNo;

});

// 삭제 버튼 클릭 시
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn.addEventListener("click", () => {
  // 정말 삭제할 것인지 confirm() 을 이용해서 확인
  // confirm()은 확인 클릭 시 true, 취소 클릭 시 false 반환

  // 취소 클릭 시
  if(!confirm("정말 삭제하시겠습니까?")) return;

  // 확인 클릭 시 삭제 요청 (GET 방식)
  location.href = "/todo/delete?todoNo=" + todoNo;

  // Controller 이름 : DeleteServlet
  // 삭제 성공 시 : "할 일이 삭제되었습니다" alert 창 띄우기
  // 삭제 실패 시 : "todo가 존재하지 않습니다" alert 창 띄우기
  // 성공이든 실패든 메인페이지("/")로 redirect
});

// 수정 버튼 클릭 시
const updateBtn = document.querySelector("#updateBtn");
updateBtn.addEventListener("click", () => {
  // 수정할 수 있는 화면을 요청 (GET 방식)
  location.href = "/todo/update?todoNo=" + todoNo;
});

