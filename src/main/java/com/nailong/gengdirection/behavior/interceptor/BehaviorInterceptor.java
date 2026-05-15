package com.nailong.gengdirection.behavior.interceptor;

import com.nailong.gengdirection.behavior.entity.ActionType;
import com.nailong.gengdirection.behavior.entity.BehaviorEvent;
import com.nailong.gengdirection.behavior.service.BehaviorCollector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 行为自动埋点拦截器。
 *
 * 不改动任何现有 Controller，仅通过 URL + 请求方法识别四类关键行为：
 * <ul>
 *   <li>GET  /posts/{id}            → VIEW（浏览帖子详情）</li>
 *   <li>POST /posts/{id}/favorite   → FAVORITE（收藏）</li>
 *   <li>POST /posts/{id}/comments   → COMMENT（评论）</li>
 *   <li>POST /posts                 → CREATE_POST（发帖）</li>
 *   <li>GET  /posts?tagId=x         → SEARCH（按标签筛选）</li>
 * </ul>
 * 只在响应成功(2xx)时记录。userId 优先取查询参数 userId，取不到记 0。
 */
@Component
@RequiredArgsConstructor
public class BehaviorInterceptor implements HandlerInterceptor {

    private static final Pattern POST_ID = Pattern.compile("^/posts/(\\d+)$");
    private static final Pattern FAVORITE = Pattern.compile("^/posts/(\\d+)/favorite$");
    private static final Pattern COMMENTS = Pattern.compile("^/posts/(\\d+)/comments$");

    private final BehaviorCollector collector;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 只记成功请求，失败/异常的不算有效行为
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            return;
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();

        ActionType action = null;
        Long postId = 0L;

        Matcher m;
        if ("GET".equals(method) && (m = POST_ID.matcher(uri)).matches()) {
            action = ActionType.VIEW;
            postId = Long.parseLong(m.group(1));
        } else if ("POST".equals(method) && (m = FAVORITE.matcher(uri)).matches()) {
            action = ActionType.FAVORITE;
            postId = Long.parseLong(m.group(1));
        } else if ("POST".equals(method) && (m = COMMENTS.matcher(uri)).matches()) {
            action = ActionType.COMMENT;
            postId = Long.parseLong(m.group(1));
        } else if ("POST".equals(method) && "/posts".equals(uri)) {
            action = ActionType.CREATE_POST;
        } else if ("GET".equals(method) && "/posts".equals(uri)
                && request.getParameter("tagId") != null) {
            action = ActionType.SEARCH;
        }

        if (action == null) {
            return;
        }
        collector.collect(BehaviorEvent.of(parseUserId(request), action, postId));
    }

    private Long parseUserId(HttpServletRequest request) {
        String raw = request.getParameter("userId");
        if (raw == null || raw.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
