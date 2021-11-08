local token = redis.call('lpop', 'snatch_time_bucket');
local curTime = tonumber(KEYS[1])      -- lua 的数组下标从 1 开始

if token~=false then
    if (tonumber(token) > curTime) then
        redis.call('lpush', 'snatch_time_bucket', token)
        return -1                      -- 未抢到红包，令牌放回去
    else
        return tonumber(token)         -- 抢到红包，返回时间戳
    end
else
    return -2                          -- 没有红包了
end