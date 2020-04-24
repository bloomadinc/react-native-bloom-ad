let isInit = false;

export function checkInit() {
  if (!isInit) {
    console.error("please init appId");
  }
  return isInit;
}

export function setInit(init = false) {
  isInit = init;
}

export function getInit() {
  return isInit;
}

export function getEventName(name = "") {
  return "_" + new Date().getTime() + "_" + name;
}
