-- event & campaign seed data

-- sequence 동기화
SELECT setval('public.event_id_seq', 11);
SELECT setval('public.event_field_id_seq', 34);
SELECT setval('public.campaign_id_seq', 5);
SELECT setval('public.campaign_filter_id_seq', 2);

COPY public.event (id, created_at, updated_at, description, event_name, is_active) FROM stdin;
1	2026-05-06 23:03:55.247416	2026-05-06 23:03:55.247416	사용자가 상품 구매 버튼을 클릭했을 때 발생하는 이벤트	purchase_button_click	t
2	2026-05-06 23:03:55.426943	2026-05-06 23:03:55.426943	사용자가 상품을 찜하거나 장바구니에 추가 또는 삭제할 때 발생하는 이벤트	wishlist_cart_action	t
3	2026-05-06 23:03:55.489423	2026-05-06 23:03:55.489423	사용자가 쿠폰을 발급받을 때 발생하는 이벤트	coupon_received	t
4	2026-05-06 23:03:55.566236	2026-05-06 23:03:55.566236	사용자가 결제 시 쿠폰을 적용할 때 발생하는 이벤트	coupon_used	t
5	2026-05-06 23:03:55.62299	2026-05-06 23:03:55.62299	사용자가 광고 배너 또는 광고 상품을 클릭할 때 발생하는 이벤트	ad_click	t
6	2026-05-06 23:03:55.646028	2026-05-06 23:03:55.646028	사용자가 검색어를 입력하고 검색을 실행할 때 발생하는 이벤트	search_button_click	t
7	2026-05-06 23:03:55.661984	2026-05-06 23:03:55.661984	사용자가 특정 페이지에 진입할 때 발생하는 이벤트	page_view	t
8	2026-05-06 23:03:55.683911	2026-05-06 23:03:55.683911	사용자가 특정 상품의 상세 페이지로 이동할 때 발생하는 이벤트	product_detail_view	t
9	2026-05-06 23:03:55.71015	2026-05-06 23:03:55.71015	사용자가 리뷰 작성 후 등록 버튼을 클릭할 때 발생하는 이벤트	review_submit_click	t
10	2026-05-06 23:03:55.736473	2026-05-06 23:03:55.736473	사용자에게 포인트가 적립될 때 발생하는 이벤트	point_earned	t
11	2026-05-06 23:03:55.826923	2026-05-06 23:03:55.826923	사용자가 결제 시 포인트를 차감하여 사용할 때 발생하는 이벤트	point_used	f
\.

COPY public.event_field (id, description, field_name, field_type, is_required, event_id) FROM stdin;
1	결제 승인된 최종 금액 (원 단위)	approvedAmount	NUMBER	t	1
2	구매한 상품의 이름	productName	STRING	t	1
3	상품 고유 식별자	productId	STRING	t	1
4	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	1
5	찜/장바구니 대상 상품명	productName	STRING	t	2
6	상품 고유 식별자	productId	STRING	t	2
7	add / remove 구분값	actionType	STRING	t	2
8	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	2
9	발급된 쿠폰의 고유 코드	couponCode	STRING	t	3
10	쿠폰 적용 시 할인되는 금액 (원 단위)	discountAmount	NUMBER	t	3
11	쿠폰 유효 만료 날짜 — 만료율 분석에 유용	expiryDate	DATE	f	3
12	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	3
13	사용된 쿠폰의 고유 코드	couponCode	STRING	t	4
14	실제 적용된 할인 금액 (원 단위)	discountAmount	NUMBER	t	4
15	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	4
16	광고된 상품명	productName	STRING	t	5
17	광고 상품 고유 식별자	productId	STRING	t	5
18	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	5
19	사용자가 입력한 검색어	searchKeyword	STRING	t	6
20	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	6
21	진입한 페이지의 이름 또는 경로	pageName	STRING	t	7
22	페이지 체류시간 (초 단위)	dwellTime	TIME	t	7
23	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	7
24	조회한 상품명	productName	STRING	t	8
25	상품 고유 식별자	productId	STRING	t	8
26	상세 페이지 체류시간 (초 단위)	dwellTime	TIME	t	8
27	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	8
28	사용자가 부여한 별점 (1~5 정수)	reviewRating	NUMBER	t	9
29	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	9
30	이번에 적립된 포인트 수	earnedPoints	NUMBER	t	10
31	적립 원인 (구매완료 / 리뷰작성 / 출석 등)	earnReason	STRING	t	10
32	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	10
33	이번 거래에서 사용된 포인트 수	usedPoints	NUMBER	t	11
34	해당 이벤트 발생 시간	eventTimestamp	DATETIME	t	11
\.

COPY public.campaign (id, created_at, updated_at, batch_cycle, campaign_goal_type, campaign_name, campaign_type, created_by, customer_segment, description, ended_at, is_duplicate, started_at, status, filter_logical_operator) FROM stdin;
5	2026-05-08 14:43:57.62071	2026-05-08 14:43:57.62071	30	EARLY_RETENTION	test	BATCH	\N	NEW	testtest	2026-05-08 23:59:59	t	2026-05-08 00:00:00	PENDING	\N
\.

COPY public.campaign_filter (id, operator, period_days, value, campaign_id, event_field_id) FROM stdin;
2	EQUALS	10	5000	5	1
\.