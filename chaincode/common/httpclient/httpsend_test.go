package httpclient

import (
	"bytes"
	"encoding/json"
	"fmt"
	"testing"
)

//go test -v -run Test_Get
func Test_Get(t *testing.T) {
	h := NewHttpSend(GetUrlBuild("http://192.168.10.76/lotus/lotustest", map[string]string{"name": "xiaochuan"}))
	body, err := h.Get()
	if err != nil {
		t.Error("请求错误:", err)
		//t.Errorf(err.Error())
		t.Fail()
	} else {
		t.Log("正常返回")

		var out bytes.Buffer
		err = json.Indent(&out, body, "", "\t")
		fmt.Println(out.String())
	}
}

func Test_Post(t *testing.T) {
	h := NewHttpSend("http://192.168.10.76/lotus/lotustest")
	h.SetBody(map[string]string{"name": "xxx"})
	body, err := h.Post()
	if err != nil {
		t.Error("请求错误:", err)
		//t.Errorf(err.Error())
		t.Fail()
	} else {
		t.Log("正常返回")

		var out bytes.Buffer
		err = json.Indent(&out, body, "", "\t")
		fmt.Println(out.String())
	}
}

func Test_Json(t *testing.T) {
	h := NewHttpSend("http://192.168.10.76/lotus/lotustest")
	h.SetSendType("JSON")
	h.SetBody(map[string]string{"name": "xxx"})
	body, err := h.Post()
	if err != nil {
		t.Error("请求错误:", err)
		//t.Errorf(err.Error())
		t.Fail()
	} else {
		t.Log("正常返回")
		var out bytes.Buffer
		err = json.Indent(&out, body, "", "\t")
		fmt.Println(out.String())
	}
}

func Benchmark_GET(b *testing.B) {
	for i := 0; i < b.N; i++ {
		h := NewHttpSend(GetUrlBuild("http://192.168.10.76/lotus/lotustest", map[string]string{"name": "xxx"}))
		_, err := h.Get()
		if err != nil {
			b.Error("请求错误:", err)
		} else {
			b.Log("正常返回")
		}
	}
}
