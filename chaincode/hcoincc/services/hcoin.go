package services

import (
	"fmt"
	"strings"
	"hmxq.top/bluestar/chaincode/common"

)

//通过组合(composite)实现类似继承(extends)和重写(override)的功能
type HCoin struct {
	common.LotusCoin //匿名struct实现类似继承功能
}

//重写方法 MakeKey
func (hc *HCoin) MakeKey(id string) string {
	return common.LC_HCOIN + strings.TrimSpace(id)
}

//实例化
func NewProvider() /* *HCoin */ common.CoinAccess {
	fmt.Printf("NewProvider()\n")

	//return &HCoin{common.LotusCoin{Label: "hcoin"}}
	var hc = HCoin{common.LotusCoin{Label:"hcoin"}}
	//避免重写无效，nil pointer dereference，golang本身的问题
	hc.CoinAccess = &hc
	return &hc
}
