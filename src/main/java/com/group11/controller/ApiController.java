package com.group11.controller;

import com.group11.common.utils.R;
import com.group11.pojo.dto.Envelope;
import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Xu Haitong
 * @since 2021/11/3 13:31
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/")
    public String hello() {
        return "服务成功启动";
    }


    @PostMapping("/snatch")
    public R snatch(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        log.info("抢红包接收到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        SnatchResponse response = new SnatchResponse(123L, 5L, (long) random.nextInt(1000));
        return R.ok().put("data", response);
    }


    @PostMapping("/open")
    public R open(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        log.info("开红包接收到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        return R.ok().put("data", new OpenResponse((long) random.nextInt(1000)));
    }


    @PostMapping("/get_wallet_list")
    public R getWalletList(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        log.info("获取钱包列表接收到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        List<Envelope> list = new ArrayList<>();

        list.add(new Envelope(123L, (long) random.nextInt(1000), true, (long) random.nextInt(1000)));
        list.add(new Envelope(123L, null, false, (long) random.nextInt(1000)));
        return R.ok().put("data", new GetWalletListResponse((long) random.nextInt(1000), list));
    }
}
