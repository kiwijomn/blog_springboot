package com.study.utils;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageMaker {
	// mysql 들어갈 값은 page 가 아니라 pageStart, perPageNum 이다.
	// limit #{pageStart}, #{perPageNum}
	private int page;

	private int perPageNum;

	private Integer pageStart;

	// 하단 페이징 << 1 2 3 4 5 6 7 8 9 10 >>
	private int totalCount; // 전체 개수

	private int startPage; // 시작 페이지

	private int endPage; // 끝페이지

	private boolean prev; // 이전 여부

	private boolean next; // 다음 여부

	private int displayPageNum = 3;

	private int tempEndPage; // 마지막 페이지

	// 검색처리 추가
	private String searchType;

	private String keyword;

	private Integer calTotal;

	private Integer calEndPage;

	private String query;

	private String searchRange;

	private String parmaRole;

	private String boardType;
	
	private int bno;
	
	private int uid;

	public PageMaker() {
		this.page = 1; // 초기 페이지는 1
		this.perPageNum = 10; // limit 10 개씩 보여준다.
	}

	public void setPage(int page) {
		// 페이지 번호가 0이거나 0보다 작으면 1페이지로 한다.
		//
		if (page <= 0) {
			this.page = 1;
			return;
		}
		this.page = page;
	}

	// MyBatis SQL 의 Mapper 에서 인식해서 가져가는 파라미터 값 메소드 #{perPageNum}
	public void setPerPageNum(int perPageNum) {
		// 몇개 씩 보여줄 것인가 이다. 최대 100개씩 보여 줄것으로 설정한다.
		// 만약 0보다 작거나 100 보다 크면 10으로 초기화 시킨다.
		if (perPageNum <= 0 || perPageNum > 1000) {
			this.perPageNum = 10;
			return;
		}
		this.perPageNum = perPageNum;
	}

	// MyBatis SQL 의 Mapper 에서 인식해서 가져가는 파라미터 값 메소드 #{pageStart}
	public Integer getPageStart() {
		// 실질적으로 Mybatis 에서 파라미터로 인식해서 가져오는 것은 get 이다.
		// 따라서 getPageStart 에서 값을 설정한다.
		// 시작 데이터 번호 = (페이지 번호 -1 ) * 페이지당 보여지는 개수
		this.pageStart = (this.page - 1) * perPageNum;
		return this.pageStart;
	}

	// 전체 페이지 설정과 동시에 하단에 뿌려질 페이지 계산하기
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}

	private void calcData() {
		// 현재 페이지 번호 / 하단 페이지번호 수
		endPage = (int) (Math.ceil(page / (double) displayPageNum) * displayPageNum);

		startPage = (endPage - displayPageNum) + 1;

		tempEndPage = (int) (Math.ceil(totalCount / (double) perPageNum));

		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}

		prev = startPage == 1 ? false : true;
		next = endPage * perPageNum >= totalCount ? false : true;
	}

	// 일반적인 페이징 처리 파라미터 출력 데이터 ex) /memberList?page=4&perPageNum=10
	public String makeQuery(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().queryParam("page", page)
				.queryParam("perPageNum", perPageNum).build();
		return uriComponents.toUriString();
	}

	// 일반적인 페이징 부트스트랩 출력
	public String bootStrapPagingHTML(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination'>");
		if (prev) {
			sBuffer.append("<li><a href='" + url + makeQuery(1) + "'>처음</a></li>");
		}

		if (prev) {
			sBuffer.append("<li><a href='" + url + makeQuery(startPage - 1) + "'>&laquo;</a></li>");
		}

		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
			} else {
				active = "";
			}
			sBuffer.append("<li " + active + " >");
			sBuffer.append("<a href='" + url + makeQuery(i) + "'>" + i + "</a></li>");
			sBuffer.append("</li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeQuery(endPage + 1) + "'>&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeQuery(tempEndPage) + "'>마지막</a></li>");
		}

		sBuffer.append("</ul>");
		return sBuffer.toString();
	}

	// 검색 추가 페이지 파라미터
	public String makeSearch(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().queryParam("page", page)
				.queryParam("perPageNum", perPageNum).queryParam("searchType", searchType)
				.queryParam("keyword", keyword).build();
		return uriComponents.toUriString();
	}

	// 검색 추가 페이징 부트스트랩 출력
	public String bootStrapPagingSearchHTML(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination'>");
		if (prev) {
			sBuffer.append("<li><a href='" + url + makeSearch(1) + "'>처음</a></li>");
		}

		if (prev) {
			sBuffer.append("<li><a href='" + url + makeSearch(startPage - 1) + "'>&laquo;</a></li>");
		}

		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
			} else {
				active = "";
			}
			sBuffer.append("<li " + active + " >");
			sBuffer.append("<a href='" + url + makeSearch(i) + "'>" + i + "</a></li>");
			sBuffer.append("</li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeSearch(endPage + 1) + "'>&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeSearch(tempEndPage) + "'>마지막</a></li>");
		}

		sBuffer.append("</ul>");
		return sBuffer.toString();
	}

	// 검색 추가 페이지 파라미터
	public String makeSearch2(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().queryParam("page", page)
				.queryParam("perPageNum", perPageNum).queryParam("searchType", searchType)
				.queryParam("keyword", keyword).queryParam("searchRange", searchRange).build();
		return uriComponents.toUriString();
	}

	// 검색 추가 페이징 부트스트랩 출력
	public String bootStrapPagingSearchHTML2(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination justify-content-center'>");
		if (prev) {
			sBuffer.append("<li class='page-item' ><a href='" + url + makeSearch2(1) + "'>처음</a></li>");
		}

		if (prev) {
			sBuffer.append("<li class='page-item'><a href='" + url + makeSearch2(startPage - 1) + "'>&laquo;</a></li>");
		}

		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class='page-item active'";
			} else {
				active = "class='page-item'";
			}
			sBuffer.append("<li " + active + " >");
			sBuffer.append("<a href='" + url + makeSearch2(i) + "'>" + i + "</a></li>");
			sBuffer.append("</li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li class='page-item'><a href='" + url + makeSearch2(endPage + 1) + "'>&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li class='page-item'><a href='" + url + makeSearch2(tempEndPage) + "'>마지막</a></li>");
		}

		sBuffer.append("</ul>");
		return sBuffer.toString();
	}
	
	
	public String inListPagination(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination board-paging'>");
		if (prev) {
			sBuffer.append("<li><a href='#'  data-page='1' >처음</a></li>");
		}

		if (prev) {
			sBuffer.append("<li><a href='#' data-page='"+(startPage-1)+"'>&laquo;</a></li>");
		}
		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
			} else {
				active = "";
			}
			sBuffer.append("<li " + active + " >");
			sBuffer.append("<a href='#'  data-page='"+i+"' >" + i + "</a></li>");
			sBuffer.append("</li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='#' data-page='"+(endPage + 1)+"'>&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='#' data-page='"+tempEndPage+"'>마지막</a></li>");
		}

		sBuffer.append("</ul>");
		return sBuffer.toString();
	}


	public String bootStrapPagingSearchHTML3(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<div class='pagination'><ul>");
		if (prev) {
			sBuffer.append("<li><a href='" + url + makeSearch2(1) + "'>처음</a></li>");
		}

		if (prev) {
			sBuffer.append("<li><a href='" + url + makeSearch2(startPage - 1) + "'>&laquo;</a></li>");
		}

		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
			} else {
				active = "";
			}
			sBuffer.append("<li " + active + " >");
			sBuffer.append("<a href='" + url + makeSearch2(i) + "'>" + i + "</a></li>");
			sBuffer.append("</li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeSearch2(endPage + 1) + "'>&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a href='" + url + makeSearch2(tempEndPage) + "'>마지막</a></li>");
		}

		sBuffer.append("</ul></div>");
		return sBuffer.toString();
	}

	// 검색 추가 페이징 부트스트랩 출력
	public String bootStrapPagingSearchHTML4(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination board-paging'>");
		if (prev) {
			sBuffer.append("<li><a style='color:#666;' href='" + url + makeSearch2(1) + "'>&laquo;</a></li>");
		}

		if (prev) {
			sBuffer.append(
					"<li><a  style='color:#666;' href='" + url + makeSearch2(startPage - 1) + "'>&lt;</a></li>");
		}

		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
				sBuffer.append("<li " + active + " >");
				sBuffer.append("<a style='color:#fff;' href='" + url + makeSearch2(i) + "'>" + i + "</a></li>");
				sBuffer.append("</li>");
			} else {
				active = "";
				sBuffer.append("<li " + active + " >");
				sBuffer.append("<a style='color:#666;' href='" + url + makeSearch2(i) + "'>" + i + "</a></li>");
				sBuffer.append("</li>");
			}

		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a style='color:#666;' href='" + url + makeSearch2(endPage + 1) + "'>&gt;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a  style='color:#666;'href='" + url + makeSearch2(tempEndPage) + "'>&raquo;</a></li>");
		}

		sBuffer.append("</ul>");
		return sBuffer.toString();
	}
	
	public String replyPagination(String url) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<ul class='pagination board-paging reply-pagination'>");
		if (prev) {
			sBuffer.append("<li><a style='color:#666;' href='#' data-page='1'   >처음</a></li>");
		}
		if (prev) {
			sBuffer.append(
					"<li><a  style='color:#666;'href='#'  data-page='" +(startPage-1)+ "' >&laquo;</a></li>");
		}
		String active = "";
		for (int i = startPage; i <= endPage; i++) {
			if (page == i) {
				active = "class=active";
				sBuffer.append("<li " + active + " >");
				sBuffer.append("<a style='color:#fff;' href='#' data-page='" + i + "'   >" + i + "</a></li>");
				sBuffer.append("</li>");
			} else {
				active = "";
				sBuffer.append("<li " + active + " >");
				sBuffer.append("<a style='color:#666;' href='#' data-page='"+i+"'  >" + i + "</a></li>");
				sBuffer.append("</li>");
			}
		}
		if (next && endPage > 0) {
			sBuffer.append("<li><a style='color:#666;'  href='#' data-page='" + (endPage + 1) + "'  >&raquo;</a></li>");
		}

		if (next && endPage > 0) {
			sBuffer.append("<li><a  style='color:#666;'  href='#' data-href='" + tempEndPage+ "'  >마지막</a></li>");
		}
		sBuffer.append("</ul>");
		return sBuffer.toString();
	}

}
