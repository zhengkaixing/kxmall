import NP from 'number-precision'

export const add = (...args) => {
  return NP.plus(...args)
}

export const multiply = (...args) => {
  return NP.times(...args)
}
