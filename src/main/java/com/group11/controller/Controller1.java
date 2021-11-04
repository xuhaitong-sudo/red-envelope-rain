package com.group11.controller;

import com.group11.common.utils.R;
import com.group11.pojo.dto.Envelope;
import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Xu Haitong
 * @since 2021/11/3 20:52
 */
@RestController
@RequestMapping("/1")
public class Controller1 {
    @PostMapping("/snatch")
    public R snatch(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        System.out.println("接受到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        SnatchResponse response = new SnatchResponse(123L, 5L, (long) random.nextInt(1000));
        return R.ok().put("data", response);
    }

    @PostMapping("/open")
    public R open(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        System.out.println("接受到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        return R.ok().put("data", new OpenResponse((long) random.nextInt(1000)));
    }

    @PostMapping("/get_wallet_list")
    public R getWalletList(@RequestBody Map<String, String> json) {
        Long uid = Long.parseLong(json.get("uid"));
        Long envelopeId = Long.parseLong(json.get("envelope_id"));
        System.out.println("接受到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        List<Envelope> list = new ArrayList<>();

        list.add(new Envelope(123L, (long) random.nextInt(1000), true, (long) random.nextInt(1000)));
        list.add(new Envelope(123L, null, false, (long) random.nextInt(1000)));
        return R.ok().put("data", new GetWalletListResponse((long) random.nextInt(1000), list));
    }
}
