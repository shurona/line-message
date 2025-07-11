package com.shrona.line_demo.line.presentation.mvc;

import static com.shrona.line_demo.common.utils.StaticVariable.HOME_VIEW;

import com.shrona.line_demo.common.dto.ChannelForm;
import com.shrona.line_demo.common.dto.PagingForm;
import com.shrona.line_demo.line.application.ChannelService;
import com.shrona.line_demo.line.application.LineService;
import com.shrona.line_demo.line.common.exception.LineErrorCode;
import com.shrona.line_demo.line.common.exception.LineException;
import com.shrona.line_demo.line.domain.Channel;
import com.shrona.line_demo.line.domain.ChannelLineUser;
import com.shrona.line_demo.line.domain.LineUser;
import com.shrona.line_demo.line.presentation.dtos.LineUserUpdatePhoneRequestDto;
import com.shrona.line_demo.line.presentation.form.LineUserForm;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class LineUserController {

    private final LineService lineService;
    private final ChannelService channelService;

    @GetMapping("/admin/channels/{channelId}/friends/list")
    public String lineFriendView(
        @RequestParam(value = "query", required = false) String query,
        @PathVariable("channelId") Long channelId,
        @RequestParam(value = "page", defaultValue = "0") int pageNumber,
        Model model
    ) {
        Optional<Channel> channelInfo = channelService.findChannelById(channelId);
        // 채널정보가 없는 경우 그냥 홈으로 보낸다.
        if (channelInfo.isEmpty()) {
            return HOME_VIEW;
        }

        // 라인 유저 아이디 목록을 갖고 온다.
        Page<ChannelLineUser> lineUserList;
        if (query != null) {
            lineUserList = lineService.findChannelLineUserListByChannelAndQuery(
                channelInfo.get(), query, PageRequest.of(pageNumber, 20));
        } else {
            lineUserList = lineService.findChannelLineUserListByChannel(
                channelInfo.get(), PageRequest.of(pageNumber, 20));
        }

        // 모델을 등록하는 부분
        model.addAttribute("pagingInfo",
            PagingForm.of(
                lineUserList.getNumber(), lineUserList.getTotalPages()));

        // 모델 등록하는 부분
        model.addAttribute("friends", lineUserList.toList()
            .stream().map(line -> LineUserForm.of(line.getLineUser())));

        // 채널 정보를 등록해준다.
        registerChannelToModel(channelInfo.get(), model);

        return "friend/list";
    }

    /**
     * 라인 유저의 휴대전화 업데이트
     */
    @PostMapping("/admin/friends/{id}")
    public ResponseEntity<?> updateLinePhoneNumber(
        @PathVariable("id") Long lineUserId,
        @RequestBody LineUserUpdatePhoneRequestDto requestDto
    ) {

        LineUser lineUser = lineService.updateLineUserPhoneNumber(lineUserId,
            requestDto.phone());

        if (lineUser == null) {
            throw new LineException(LineErrorCode.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 모델에 채널 정보 등록
     */
    private void registerChannelToModel(Channel channel, Model model) {
        model.addAttribute("channelInfo", ChannelForm.of(channel));
    }
}
