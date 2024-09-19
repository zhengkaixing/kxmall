import dayjs from 'dayjs'
export const getTime = (str) => {
  let time = dayjs()
  const arr = str.split(':')
  if (arr.length < 3) {
    arr.push('00')
  }
  arr.forEach((el, i) => i === 0 ? (time = time.hour(el)) : (i === 1 ? (
    time = time.minute(el)) : (time = time.second(el))))
  return time
}

export const getRestTime = (time) => {
  let hour = 0,
    minute = 0,
    second = 0
  if (time) {
    const endTime = getTime(time)
    const now = dayjs()
    hour = endTime.diff(now, 'hour')
    minute = endTime.diff(now, 'minute') - hour * 60
    second = endTime.diff(now, 'second') - hour * 60 * 60 - minute * 60
  }
  return [hour, minute, second]
}