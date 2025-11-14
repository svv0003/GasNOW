/*================================================================================================
    차트
    ================================================================================================*/
let chart = null;

const areaCodeToName = {
    "01" : "서울",
    "02" : "경기",
    "03" : "강원",
    "04" : "충북",
    "05" : "충남",
    "06" : "전북",
    "07" : "전남",
    "08" : "경북",
    "09" : "경남",
    "10" : "부산",
    "11" : "제주",
    "14" : "대구",
    "15" : "인천",
    "16" : "광주",
    "17" : "대전",
    "18" : "울산",
    "19" : "세종"
};
/*
async function updateChart() {
    const selectedKoreanArea = areaCodeToName[selectedArea];
    // 선택된 유종, 지역, 기간으로 API URL 결정한다.
    const chartURL = `http://localhost:8080/api/chart/data?oilCategory=${selectedOil}&areaName=${selectedKoreanArea}&period=${selectedPeriod}`;
    try {
        const res = await fetch(chartURL);
        if (!res.ok) {
            throw new Error("데이터를 가져오는데 실패했습니다");
            console.log("데이터 가져오는데 실패");
        }
        const chartData = await res.json();
        console.log("서버 응답 데이터:", chartData);
        document.getElementById('loading').style.display = 'none';
        document.getElementById('tabs').style.display = 'flex';
        document.getElementById('chartContainer').style.display = 'block';
        document.getElementById('legend').style.display = 'flex';
        renderChart(chartData, selectedArea);
        console.log("selectedArea (코드):", selectedArea);           // "01"
        console.log("selectedKoreanArea (한글):", selectedKoreanArea); // "서울"
        console.log("응답 데이터 키:", Object.keys(chartData.week));   // ["labels", "전국", "서울"]
    } catch (err) {
        console.error("에러:", err);
        document.getElementById("loading").style.display = 'none';
        document.getElementById('error').textContent = '데이터를 불러오는데 실패했습니다: ' + err.message;
        document.getElementById('error').style.display = 'block';
    }
}
 */
async function updateChart() {
    const loading = document.getElementById('loading');
    const error = document.getElementById('error');
    const tabs = document.getElementById('tabs');
    const chartContainer = document.getElementById('chartContainer');
    const legend = document.getElementById('legend');
    const localAreaName = document.getElementById('localAreaName');

    // 초기화
    loading.style.display = 'block';
    error.textContent = '';
    error.style.display = 'none';
    tabs.style.display = 'none';
    chartContainer.style.display = 'none';
    legend.style.display = 'none';

    const selectedKoreanArea = areaCodeToName[selectedArea];
    if (!selectedKoreanArea) {
        loading.style.display = 'none';
        error.textContent = '지역을 선택해주세요.';
        error.style.display = 'block';
        return;
    }

    $("#localName").text(selectedKoreanArea);
    const chartURL = `http://localhost:8080/api/chart/data?oilCategory=${selectedOil}&areaName=${selectedKoreanArea}&period=${selectedPeriod}`;
    console.log("요청 URL:", chartURL);

    try {
        const res = await fetch(chartURL);
        if (!res.ok) {
            throw new Error(`서버 오류: ${res.status}`);
        }

        const chartData = await res.json();
        console.log("서버 응답 데이터:", chartData);

        // 데이터 유효성 검사 (핵심!)
        if (!chartData || typeof chartData !== 'object') {
            throw new Error("응답 데이터가 비어 있습니다.");
        }

        // period 키 추출 (week, month, year, all)
        const periodKey = selectedPeriod; // 백엔드에서 period 그대로 키로 사용
        if (!chartData[periodKey]) {
            throw new Error(`기간 데이터 없음: ${periodKey}`);
        }

        const periodData = chartData[periodKey];
        if (!periodData.labels || !periodData.전국 || !periodData[selectedKoreanArea]) {
            throw new Error("필수 데이터 누락");
        }

        // UI 보이기
        loading.style.display = 'none';
        tabs.style.display = 'flex';
        chartContainer.style.display = 'block';
        legend.style.display = 'flex';
        if (localAreaName) localAreaName.textContent = selectedKoreanArea;

        // 차트 렌더링
        renderChart(chartData, selectedArea, periodKey);

        // 차트 강제 리사이즈
        setTimeout(() => {
            if (chart) chart.resize();
        }, 100);

        console.log("selectedArea (코드):", selectedArea);
        console.log("selectedKoreanArea (한글):", selectedKoreanArea);
        console.log("응답 데이터 키:", Object.keys(periodData));

    } catch (err) {
        loading.style.display = 'none';
        error.textContent = err.message || '데이터를 불러오는데 실패했습니다.';
        error.style.display = 'block';
        console.error("에러:", err);
    }
}
/*==========================================
차트 데이터와 지역 값을 받아와서 차트 렌더링한다.
기존 존재하던 차트는 제거한다.
==========================================*/
function renderChart(data, areaCode, periodKey) {
    console.log("renderChart 시작");

    const canvas = document.getElementById('priceChart');
    if (!canvas) {
        console.error("캔버스 요소 없음! #priceChart 확인");
        return;
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) {
        console.error("2D 컨텍스트 생성 실패");
        return;
    }

    if (chart) {
        console.log("기존 차트 제거");
        chart.destroy();
    }

    const chartData = data[periodKey];
    const koreanArea = areaCodeToName[areaCode];
    if (!koreanArea) return console.error("매핑 실패:", areaCode);
    if (!chartData[koreanArea]) return console.error("데이터 없음:", koreanArea);

    const xTicksOptions = {
        grid: { display: false },
        ticks: {
            autoSkip: true, // 겹치면 자동 건너뛰기 (기본값)
            padding: 10,    // 레이블과 차트 축 사이의 간격
        }
    };

    if (periodKey === 'year') {
        xTicksOptions.ticks.maxTicksLimit = 13;
    } else if (periodKey === 'all') {
        xTicksOptions.ticks.maxTicksLimit = 12;
    }

    console.log("차트 데이터 준비 완료", {
        labels: chartData.labels.length,
        전국: chartData.전국.length,
        [koreanArea]: chartData[koreanArea].length
    });

    try {
        chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: chartData.labels,
                datasets: [
                    {
                        label: '전국',
                        data: chartData['전국'] || [],
                        borderColor: '#0d47a1',
                        backgroundColor: '#0d47a1',
                        tension: 0.4,
                        borderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 4,
                        pointBackgroundColor: '#0d47a1',
                        pointBorderColor: '#fff',
                        pointBorderWidth: 2,
                        fill: false
                    },
                    {
                        label: koreanArea,
                        data: chartData[koreanArea] || [],
                        borderColor: '#ff00ea',
                        backgroundColor: '#ff00ea',
                        tension: 0.4,
                        borderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6,
                        pointBackgroundColor: '#ff00ea',
                        pointBorderColor: '#fff',
                        pointBorderWidth: 2,
                        fill: false
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        enabled: true,
                        backgroundColor: 'rgba(0, 0, 0, 0.8)',
                        padding: 12,
                        titleFont: {
                            size: 13
                        },
                        bodyFont: {
                            size: 13
                        },
                        callbacks: {
                            label: function (context) {
                                return context.dataset.label + ': ' + context.parsed.y + '원';
                            }
                        }
                    }
                },
                scales: {
                    // y: { min: 1640, max: 1740, ticks: { stepSize: 10 } },
                    // y: { grace: '10%' }, // 데이터 기준 min/max 값에서 10%의 여유 공간을 줌
                    // x: { grid: { display: false } }
                    // x: xTicksOptions
                    y: {
                        ticks: {
                            font: {
                                size: 11
                            }
                        },
                        grid: {
                            color: '#f0f0f0'
                        },
                        grace:
                            '10%',
                    },
                    x: {
                        type: 'category',
                        ticks: {
                            font: {
                                size: 11
                            },
                            maxRotation: 45,
                            minRotation: 45
                        },
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });
        console.log("차트 생성 성공!", chart);
    } catch (err) {
        console.error("Chart.js 생성 실패:", err);
    }
}

/*===============================================================================================
차트 슬라이드 기능
- 타이머 기능으로 순차적으로 다음 지역 데이터를 호출해서 렌더링한다.
- 지역, 기간, 유종 등을 클릭 시 슬라이드 기능 중지된다.
- 더블 클릭 시 슬라이드 기능 다시 시작한다.
// 1. 순회할 모든 지역 코드
const allAreaCodes = ["01","02","03","04","05","06","07","08","09","10","11","14","15","16","17","18","19"];
// 2. 타이머 ID를 저장할 변수
let autoCycleTimer = null;
// 3. 현재 순회 중인 지역 인덱스
let currentAreaIndex = 0;

function startAutoCycle() {
    stopAutoCycle();
    console.log("슬라이드");
    autoCycleTimer = setInterval(() => {
        selectedArea = allAreaCodes[currentAreaIndex];
        updateChart();
        currentAreaIndex++;
        if (currentAreaIndex >= allAreaCodes.length+2) {
            currentAreaIndex = 0;
        }
    }, 5000);
}

function stopAutoCycle() {
    if (autoCycleTimer) {
        clearInterval(autoCycleTimer);
        autoCycleTimer = null;
        currentAreaIndex = 0;
        console.log("자동 순회 중지.");
    }
}

$('#chartContainer').on('dblclick', function() {
    console.log("차트 더블클릭 감지");
    startAutoCycle();
});
================================================================================================*/

/*================================================================================================
공통 변수, 객체, 배열
================================================================================================*/
// API
let API_KEY = null;
fetch('/api/config/key')
    .then(response => response.json())
    .then(data => {
        API_KEY = data.apiKey;
        console.log("api_key:", API_KEY);
    })
    .catch(err => console.error("API Key 불러오기 실패:", err));
const DB_URL = "http://localhost:8080/api";
const API_BASE_URL = "https://axis-convert-new.vercel.app";
// const API_KEY = "F250930867";

/*=================================================
시도별 유종별 현재가
${API_BASE_URL}/api/avg-sido-price
<RESULT>
  <OIL>
    <SIDOCD>00</SIDOCD>
    <SIDONM>전국</SIDONM>
    <PRODCD>B034</PRODCD>
    <PRICE>1910.12</PRICE>
    <DIFF>1910.12</DIFF>
  </OIL>
</RESULT>
=================================================*/
// 시도별 유종별 현재가 API 데이터를 저장할 배열
let allOilPrice = [];

// API SIDOCD SIDONM Map
let API_SIDOCD_SIDONM_Map = new Map();

// 전날 평균가 Map
let yesterdayPricesMap = new Map();

// 선택한 지역, 유종을 저장할 변수 (기본값 = 휘발유, 서울)
let selectedOil = "B027";
let selectedArea = "01";
let selectedPeriod = "week";
const defaultDiffArea = "00";

/*=================================================
HTML
<path>    id="KR11"       Seoul
<text>    id="label-KR11" Seoul
<button>  id="B027"       휘발유

API
<SIDOCD>      01          Seoul
<PRODCD>      B027        휘발유
=================================================*/
// API PRODCD : PRODNM
const PRODCD_to_Korean = {
    B034: "고급휘발유",
    B027: "휘발유",
    D047: "경유",
    C004: "등유",
    K015: "LPG",
};

// Path id : API AREACD
const pathId_to_SIDOCD = {
    KR11: "01", // 서울
    KR26: "10", // 부산
    KR27: "14", // 대구
    KR28: "15", // 인천
    KR29: "16", // 광주
    KR30: "17", // 대전
    KR31: "18", // 울산
    KR41: "02", // 경기
    KR42: "03", // 강원
    KR43: "04", // 충북
    KR44: "05", // 충남
    KR45: "06", // 전북
    KR46: "07", // 전남
    KR47: "08", // 경북
    KR48: "09", // 경남
    KR49: "11", // 제주
    KR50: "19", // 세종
};

// API 지역 코드 : Path id
const SIDOCD_to_pathId = {};

/*================================================================================================
<Path> id와 API <SIDOCD> 매핑한 객체를 Key와 Value를 반대로 다른 객체에 저장한다.
================================================================================================*/
for (const svgId in pathId_to_SIDOCD) {
    const apiCode = pathId_to_SIDOCD[svgId];
    SIDOCD_to_pathId[apiCode] = svgId;
}
// console.log("pathId_to_SIDOCD", pathId_to_SIDOCD);
// console.log("SIDOCD_to_pathId", SIDOCD_to_pathId);

/*================================================================================================
지도
1. 페이지 로드 시 API 요청하고, 응답을 확인한다.
   ${API_BASE_URL}/api/avg-sido-price
2. 요청 성공 시 API 데이터 (XML 문자열)를 XML 문서 객체로 파싱하여 저장한다.
3. 파싱된 XML 문서를 jQuery 객체러 감싸서 탐색이 쉽도록 한다.
4. <OIL> 태그를 모두 찾아서 순회하도록 한다. (each)
5. <OIL> 태그 내부의 데이터를 추출하여 객체에 저장한다.
6. 객체에 저장된 데이터를 allOilPrice 배열에 저장한다.
7. API_SIDOCD_SIDONM_Map
8. 초기 렌더링 후 기본값인 휘발유 가격을 표시한다.
================================================================================================*/
$(document).ready(function () {
    $.ajax({
        method: "GET",
        url: `${API_BASE_URL}/api/avg-sido-price`,
        data: {
            api_key: API_KEY,
        },
        success: function (data) {
            console.log("API 전체 응답:", data);
            if (data && data.xml_data) {
                try {
                    const xmlDoc = $.parseXML(data.xml_data);
                    console.log("xmlDoc:", xmlDoc);
                    const $xml = $(xmlDoc);
                    $xml.find("OIL").each(function () {
                        const $oil = $(this);
                        console.log("$oil:", $oil);
                        const oilData = {
                            SIDOCD: $oil.find("SIDOCD").text(),
                            SIDONM: $oil.find("SIDONM").text(),
                            PRODCD: $oil.find("PRODCD").text(),
                            PRICE: $oil.find("PRICE").text(),
                        };
                        console.log("oilData:", oilData);
                        allOilPrice.push(oilData);
                        if (!API_SIDOCD_SIDONM_Map.has(oilData.SIDOCD)) {
                            API_SIDOCD_SIDONM_Map.set(oilData.SIDOCD, oilData.SIDONM);
                        }
                    });
                    console.log("XML 파싱 후 allOilPrice:", allOilPrice);
                    console.log(
                        "XML 파싱 후 API_SIDOCD_SIDONM_Map:",
                        API_SIDOCD_SIDONM_Map
                    );
                    if (allOilPrice.length === 0) {
                        alert("데이터 파싱에 성공했으나, 유가 정보가 없습니다.");
                    }
                    updateDisplay(selectedOil);
                    showPrice(defaultDiffArea);
                    updateChart();
                    // setTimeout(startAutoCycle, 2000);
                } catch (parseError) {
                    console.error("XML 파싱 중 오류 발생:", parseError);
                    alert(
                        "데이터 형식이 올바르나, 내부 XML을 파싱하는 데 실패했습니다."
                    );
                }
            } else {
                console.error("API 응답에 xml_data가 없습니다:", data);
                alert("API 응답 형식이 올바르지 않습니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("API 호출 실패:", error);
            alert("데이터를 불러오는 데 실패했습니다.");
        },
    });

    /*================================================================================================
    지도 지역 클릭 시
    1. 모든 지역에서 .on 클래스를 제거하고,
       클릭한 지역에 .on 클래스를 추가한다.
    2. selectedArea 변수에 pathId_to_SIDOCD 객체에서 해당 path id로 조회한 API SIDOCD를 할당한다.
    3. selectedArea 변수를 매개변수로 차트 그래프에 사용할 예정이다.
    ================================================================================================*/
    $(".region").on("click", function () {
        $(".region").removeClass("on");
        $(this).addClass("on");
        selectedArea = pathId_to_SIDOCD[$(this).attr("id")];
        // console.log("지역 선택 (SVG pathId):", pathId);
        // console.log("지역 선택 (API SIDOCD):", selectedArea);
        // stopAutoCycle();
        updateChart();
    });

    /*================================================================================================
    유종 버튼 클릭 시
    1. 모든 유종 버튼에서 .on 클래스를 제거하고,
       클릭한 버튼에 .on 클래스를 추가한다.
    2. selectedOil 변수에 해당 버튼의 id를 할당한다.
    3. selectedOil 변수를 매개변수로 updateDisplay 함수를 호출한다.
    ================================================================================================*/
    $(".oil-btn").on("click", function () {
        $(".oil-btn").removeClass("on");
        $(".diff-box").removeClass("on");
        const btnId = $(this).attr("id");
        const btnOil = btnId.replace("Btn", "");
        $(this).addClass("on");
        $("#" + btnOil + "DiffBox").addClass("on");
        selectedOil = btnOil;
        updateDisplay(selectedOil);
        // console.log("유종 선택:", selectedOil);
        // stopAutoCycle();
        updateChart();
    });

    $(".diff-box").on("click", function () {
        $(".oil-btn").removeClass("on");
        $(".diff-box").removeClass("on");
        const boxId = $(this).attr("id");
        const boxOil = boxId.replace("DiffBox", "");
        $("#" + boxOil + "Btn").addClass("on");
        $("#" + boxOil + "DiffBox").addClass("on");
        selectedOil = boxOil;
        updateDisplay(selectedOil);
        // stopAutoCycle();
        updateChart();
        console.log(boxOil);
        console.log($("#" + "btn-" + boxOil).attr("class"));
    });

    $(".period-btn").on("click", function () {
        $(".period-btn").removeClass("on");
        $(this).addClass("on");
        selectedPeriod = $(this).attr("id");
        console.log(selectedPeriod);
        // stopAutoCycle();
        updateChart();
    });
});

/*================================================================================================
MySQL DB 데이터 조회
1. Fetch 사용해서 DB endpoint 경로 GET 요청한다.
2. 서버 응답 예외 처리한다. (404, 500 등)
3. JSON 데이터를 JavaScript 객체로 변환하여 반환한다.
================================================================================================*/
async function getData() {
    const res = await fetch(DB_URL + "/chart/yesterday");
    if (!res.ok) {
        throw new Error("서버 응답 오류: " + res.status);
    }
    return await res.json();
}

/*================================================================================================
<text> 태그 내용 업데이트
1. API_SIDOCD_SIDONM_Map에 저장된 모든 지역을 순회한다.
   forEach -> SIDOCD: '01', SIDONM: '서울'
2. SIDOCD_to_pathId 객체에서 SIDOCD로 pathId를 조회한다.
3. 조회되지 않는 pathId는 건너뛰고,
4. allOilPrice 배열에서 각 요소의 SIDOCD, PRODCD가
   API_SIDOCD_SIDONM_Map을 순회하여 조회된 SIDOCD와 동일하고,
   selectedOil 변수와 동일한 요소만 조회한다.
5. 조회한 요소에서 PRICE만 정수로 표현하여 해당 지역의 <text> 태그 내용으로 업데이트한다.
================================================================================================*/
function updateDisplay(selectedOil) {
    API_SIDOCD_SIDONM_Map.forEach((sidonm, sidocd) => {
        const path_id = SIDOCD_to_pathId[sidocd];
        if (!path_id) {
            return;
        }
        const priceData = allOilPrice.find(
            (item) => item.SIDOCD === sidocd && item.PRODCD === selectedOil
        );
        // console.log(priceData);
        let displayText = "";
        if (priceData) {
            const formattedPrice = Math.floor(priceData.PRICE).toLocaleString(
                "ko-KR"
            );
            displayText = `${formattedPrice}원`;
        }
        const label_id = $("#label-" + path_id);
        label_id.text(displayText);
    });
}

/*================================================================================================
================================================================================================*/
// [추가] 티커 롤링에 필요한 전역 변수
let currentTickerIndex = 0;
const tickerItemHeight = 40; // CSS에서 .diff-box 높이와 일치해야 함
const tickerTransitionTime = 500; // 0.5초 (CSS transition 시간)
const $tickerList = $("#tickerList"); // ⬅️ HTML <ul>의 ID
let totalTickerItems = 0;


/*================================================================================================
.diff-price 내용 업데이트
1. allOilPrice 배열에서 SIDOCD가 "00"인 요소만 필터링한다.
2. 필터링된 요소를 순회하면서 유종과 가격을 변수에 할당한다.
3. 조회한 가격과 유종을 해당 유종 현재가 <div> 태그 내용으로 업데이트한다.
4. showDiff 함수에 사용될 DB 데이터를 Map에 저장한다.
5. Map에서 유종별로 조회한 전날 평균가와 현재가를 매개변수로 showDiff 함수를 호출한다.

async function showPrice(defaultDiffArea) {
  try {
    const chartData = await getData();
    chartData.forEach((item) => {
      yesterdayPricesMap.set(item.OIL_CATEGORY, item.AVG_PRICE);
    });
    console.log("yesterdayPricesMap:", yesterdayPricesMap);
  } catch (e) {
    console.error("DB 데이터 조회 실패:", e);
  }
  const nationalPrice = allOilPrice.filter(
    (item) => item.SIDOCD === defaultDiffArea
  );
  nationalPrice.forEach((priceData) => {
    const prodcd = priceData.PRODCD;
    const currentPriceStr = priceData.PRICE;
    let displayText = "";
    if (currentPriceStr) {
      displayText =
        parseFloat(currentPriceStr).toLocaleString("ko-KR", {
          minimumFractionDigits: 0,
          maximumFractionDigits: 0,
        }) + "원";
        console.log(displayText);
    }
    const diff_price_id = $("#" + prodcd + "DiffPrice");
    diff_price_id.text(displayText);
    const yesterdayPriceStr = yesterdayPricesMap.get(prodcd);
    showDiff(prodcd, currentPriceStr, yesterdayPriceStr);
  });
}
================================================================================================*/
async function showPrice(defaultDiffArea) {
    try {
        const chartData = await getData();
        chartData.forEach((item) => {
            // SQL의 OIL_CATEGORY를 Key로, AVG_PRICE를 Value로 저장
            yesterdayPricesMap.set(item.oilCategory, item.avgPrice);
        });
        console.log("yesterdayPricesMap:", yesterdayPricesMap);
    } catch (e) {
        console.error("DB 데이터 조회 실패:", e);
    }
    const nationalPrice = allOilPrice.filter(
        (item) => item.SIDOCD === defaultDiffArea
    );
    // --- 2. [수정] <li> HTML 생성 ---
    $tickerList.empty(); // <ul> 비우기

    // (유종 이름 매핑 - 필요시)
    const oilNames = {
        "B034": "고급휘발유", "B027": "휘발유", "D047": "경유", "C004": "등유", "K015": "LPG"
    };
    nationalPrice.forEach((priceData) => {
        const prodcd = priceData.PRODCD;
        const currentPriceStr = priceData.PRICE;
        let formattedPrice = "";
        if (currentPriceStr) {
            formattedPrice =
                parseFloat(currentPriceStr).toLocaleString("ko-KR", {
                    minimumFractionDigits: 0,
                    maximumFractionDigits: 0,
                }) + "원";
            console.log(formattedPrice);
        }
        const diff_price_id = $("#" + prodcd + "DiffPrice");
        diff_price_id.text(formattedPrice);
        const yesterdayPriceStr = yesterdayPricesMap.get(prodcd);
        const diffData = showDiff(prodcd, currentPriceStr, yesterdayPriceStr);
        const oilName = oilNames[prodcd] || prodcd;
        const liHtml = `
          <li class="ticker-box">
            <div class="ticker-oil">${oilName}</div>
            <div class="ticker-price">${formattedPrice}</div>
            <div class="ticker-percent ${diffData.className}">${diffData.displayText}</div>
          </li>`;

        $tickerList.append(liHtml);
    });
    totalTickerItems = nationalPrice.length;
    if (totalTickerItems > 0) {
        const $firstItemClone = $tickerList.children().first().clone();
        $tickerList.append($firstItemClone);

        // --- 4초마다 티커 실행 ---
        setInterval(runTickerLoop, 4000);
    }
}

/*================================================================================================
.diff-percent 내용 업데이트
1. 매개변수로 받은 현재가와 전날 평균가를 숫자로 변환한다.
2. 전날 평균가가 없다면 종료하고, 있다면 차익과 백분율을 계산한다.

function showDiff(prodcd, currentPriceStr, yesterdayPriceStr) {
  const diff_percent_id = $("#" + prodcd + "DiffPercent");
  const currentPrice = parseFloat(currentPriceStr);
  const yesterdayPrice = parseFloat(yesterdayPriceStr);
  if (!yesterdayPrice || yesterdayPrice === 0) {
    diff_percent_id.text("-");
    diff_percent_id.removeClass("up down");
    return;
  }
  const calPrice = currentPrice - yesterdayPrice;
  const calPercent = (calPrice / yesterdayPrice) * 100;
  diff_percent_id.removeClass("up down");
  let displayText = "";
  if (calPrice > 0) {
    displayText = `▲${calPrice.toLocaleString(
      "ko-KR"
    )}원 (+${calPercent.toFixed(2)}%)`;
    diff_percent_id.addClass("up");
  } else if (calPrice < 0) {
    displayText = `▼${Math.abs(calPrice).toLocaleString(
      "ko-KR"
    )}원 (${calPercent.toFixed(2)}%)`;
    diff_percent_id.addClass("down");
  } else {
    displayText = `0원 (0.00%)`;
  }
  diff_percent_id.text(displayText);
}
================================================================================================*/
function showDiff(prodcd, currentPriceStr, yesterdayPriceStr) {
    const diff_percent_id = $("#" + prodcd + "DiffPercent");
    const currentPrice = parseFloat(currentPriceStr);
    const yesterdayPrice = parseFloat(yesterdayPriceStr);
    if (!yesterdayPrice || yesterdayPrice === 0) {
        diff_percent_id.text("-");
        diff_percent_id.removeClass("up down");
        return { displayText: "-", className: "" };
    }
    const calPrice = currentPrice - yesterdayPrice;
    const calPercent = (calPrice / yesterdayPrice) * 100;
    diff_percent_id.removeClass("up down");
    let displayText = "";
    let className = "";
    if (calPrice > 0) {
        displayText = `▲${calPrice.toLocaleString(
            "ko-KR"
        )}원 (+${calPercent.toFixed(2)}%)`;
        diff_percent_id.addClass("up");
        className = "up";
    } else if (calPrice < 0) {
        displayText = `▼${Math.abs(calPrice).toLocaleString(
            "ko-KR"
        )}원 (${calPercent.toFixed(2)}%)`;
        diff_percent_id.addClass("down");
        className = "down";
    } else {
        displayText = `0원 (0.00%)`;
    }
    diff_percent_id.text(displayText);
    return { displayText: displayText, className: className };
}


/*================================================================================================
티커
================================================================================================*/
// [추가] 3초마다 호출될 롤링 함수
function runTickerLoop() {
    // 1. 다음 항목으로 인덱스 이동
    currentTickerIndex++;

    // 2. <ul>를 (항목 높이 * 인덱스) 만큼 위로 이동
    $tickerList.css('transform', `translateY(-${currentTickerIndex * tickerItemHeight}px)`);

    // 3. [중요] 마지막 항목 (복제본)에 도달했는지 확인
    if (currentTickerIndex === totalTickerItems) {

        // 4. 슬라이드 애니메이션(0.5초)이 끝날 시간을 기다림
        setTimeout(() => {
            // 5. (순간이동) 애니메이션을 끄고
            $tickerList.css('transition', 'none');
            // 6. (순간이동) 위치를 0 (실제 첫 항목)으로 리셋
            $tickerList.css('transform', 'translateY(0)');
            // 7. 인덱스 리셋
            currentTickerIndex = 0;

            // 8. (중요) 다음 애니메이션을 위해 transition을 다시 켬
            setTimeout(() => {
                $tickerList.css('transition', `transform ${tickerTransitionTime}ms ease`);
            }, 50);

        }, tickerTransitionTime); // CSS transition 시간과 동일하게
    }
}

/*================================================================================================
차트 생성하기
1. HTML에서 지역 <path> 기간 <button> 선택 값을 변수에 저장한다.
2. JS에서 RestController GetMapping endpoint API를 Fetch로 데이터 요청을 보낸다.
3. RestController에서 전달 받은 선택 값을 파라미터로, DB 데이터를 조회하는 Mapper를 호출한다.
4. Spring Boot가 DB 조회 결과 (List<Chart>)를 JSON 배열로 변환하여 응답한다.
5. fetch의 .then() (또는 await 이후)에서 이 JSON 배열을 Chart.js가 요구하는 포맷(labels 배열, data 배열)으로 변환한다.
6. 변환된 데이터로 Chart.js 차트를 그리거나 업데이트합니다.
================================================================================================*/



/*================================================================================================
로그인 / 로그아웃 버튼
session에 저장된 로그인한 회원 정보가 있으면
================================================================================================*/


