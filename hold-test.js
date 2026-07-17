import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    reserve: {
      executor: 'per-vu-iterations',
      vus: 10,          // 동시에 10명
      iterations: 1,    // 각 사용자 1번만 요청
      maxDuration: '10s',
    },
  },
};

export default function () {
  const payload = JSON.stringify({
    performanceSeatIds: [1],   // 같은 좌석
    memberId: __VU,            // VU별 다른 회원
  });

  const res = http.post(
      'http://localhost:8080/api/seats/hold',
      payload,
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
  );

  check(res, {
    '응답은 200 또는 409': (r) => r.status === 200 || r.status === 409,
    '500 에러 없음': (r) => r.status !== 500,
  });

  // 결과 출력
  switch (res.status) {
    case 200:
      console.log(`✅ SUCCESS | VU=${__VU} | memberId=${__VU}`);
      break;
    case 409:
      console.log(`❌ CONFLICT | VU=${__VU} | memberId=${__VU}`);
      break;
    default:
      console.log(`⚠️ STATUS=${res.status} | BODY=${res.body}`);
  }
}