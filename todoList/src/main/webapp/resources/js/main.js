/* 제목이 작성되지 않은 경우 form 제출 막기 */
const addForm = document.querySelector("#addForm");
const title = document.querySelector("[name=title]");

// addForm 이 제출될 때, 제목이 입력되지 않은 경우 form 제출 이벤트 막기
addForm.addEventListener("submit", e => {   // addFrom이 제출될 때, (e : 이벤트 객체)

  const input = title.value.trim();         // 제목에 입력된 값 가져와서 양쪽 공백 제거

  if(input.length === 0 ){                  // 제목이 입력되지 않은 경우
    e.preventDefault();                     // form 제출 이벤트 막기
    alert("제목을 입력해주세요!");
    title.focus();
    title.value = "";
  }

});