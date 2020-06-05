// @Title 
// @Description  常量定义及部分结构
// @Date 2018.5.10
// @Author disp

package common

const (
	HF_CHANNEL = ""        //当前通道名称 "lotuschannel"，默认当前通道
	HF_LOTUSCC = "lotuscc" //主链码名
	HF_BCOINCC = "bcoincc" //分红玛链码名
	HF_HCOINCC = "hcoincc" //普通玛链码名
)

const (
	LC_USERINFO = "LC_0001_"
	LC_MEMPOINT = "LC_0002_"

	LC_HCOIN = "LC_1001_"
	LC_BCOIN = "LC_1002_"
)

const (
	LCS_SUCCESS     = "SUCCESS"
	LCS_UNSUPPORTED = "Unsupported"
)

const (
	ITEM_STATUS_ONSALE = "0"
	ITEM_STATUS_SOLD   = "1"
	CASH_PROD_ID       = "99999999"
)

const (
	MIN_USERID       = "00000000"          //用户代码最小值
	MAX_USERID       = "99999999"          //用户代码最大值
	MIN_LOG_CASHID   = "00000000000000000" //交易ID最小值（查询资金明细时使用）
	MAX_LOG_CASHID   = "99999999999999999" //交易ID最大值（查询资金明细时使用）
	MIN_TRANCTION_ID = "00000000000000000" //交易ID最小值（查询交易明细时使用）
	MAX_TRANCTION_ID = "99999999999999999" //交易ID最大值（查询交易明细时使用）
	MIN_PROD_ID      = "00000000"          //产品ID最小值（查询交易明细时使用）
	MAX_PROD_ID      = "99999999"          //产品ID最大值（查询交易明细时使用）
)

type Userinfo struct {
	User_ID    string `json:"user_id"`    //用户ID
	Name       string `json:"name"`       //用户姓名
	Phone      string `json:"phone"`      //手机号
	CreateTime string `json:"createtime"` //用户创建时间
	Remarks    string `json:"remarks"`    //用户备注信息
	Reserve1   string `json:"reserve1"`   //预留字段1

	LastUpdateTime string `json:"lastupdatetime"` //最后更新时间 "2006-01-02 15:04:05"
}

type CoinInfo struct {
	User_ID   string `json:"user_id"`   //用户ID
	Balance   int64  `json:"balance"`   //余额i
	Balance_s string `json:"balance_s"` //余额s
	Remarks   string `json:"remarks"`   //积分来源备注信息
	Reserve1  string `json:"reserve1"`  //预留字段1

	LastUpdateTime string `json:"lastupdatetime"` //最后更新时间 "2006-01-02 15:04:05"
}
