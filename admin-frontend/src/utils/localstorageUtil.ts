type DefaultValueType = '{}' | '[]'

export function getItem<T>(key: string, defaultValue: DefaultValueType): T {
  return JSON.parse(localStorage.getItem(key) || defaultValue) as T
}

export function setItem(key: string, value: Object | Array<any>): void {
  localStorage.setItem(key, JSON.stringify(value))
}

export function removeItem(key: string): void {
  localStorage.removeItem(key)
}
