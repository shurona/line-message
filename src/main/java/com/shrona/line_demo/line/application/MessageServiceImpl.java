package com.shrona.line_demo.line.application;

import com.shrona.line_demo.line.application.utils.MessageUtils;
import com.shrona.line_demo.line.domain.MessageLog;
import com.shrona.line_demo.line.domain.MessageType;
import com.shrona.line_demo.line.infrastructure.MessageLogJpaRepository;
import com.shrona.line_demo.line.infrastructure.MessageTypeJpaRepository;
import com.shrona.line_demo.user.application.GroupService;
import com.shrona.line_demo.user.domain.Group;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    // repository
    private final MessageLogJpaRepository messageLogRepository;
    private final MessageTypeJpaRepository messageTypeRepository;

    // service
    private final GroupService groupService;

    // Utils
    private final MessageUtils messageUtils;


    @Transactional
    public MessageType createMessageType(String title, String text) {
        Optional<MessageType> mt = messageTypeRepository.findByTitle(title);
        MessageType messageType = MessageType.of(title, text);
        return mt.orElseGet(() -> messageTypeRepository.save(messageType));
    }

    @Transactional
    public List<MessageLog> createMessage
        (Long messageTypeId, List<Long> groupIds, LocalDateTime reserveTime, String content) {

        Optional<MessageType> typeInfo = messageTypeRepository.findById(messageTypeId);
        List<Group> groupInfo = groupService.findGroupByIdList(groupIds);

        if (typeInfo.isEmpty() || groupInfo.isEmpty()) {
            return null;
        }

        List<MessageLog> messageLogList = messageLogRepository.saveAll(groupInfo.stream()
            .map(g -> MessageLog.messageLog(typeInfo.get(), g, reserveTime, content)).toList());

        messageUtils.registerTaskSchedule(messageLogList, reserveTime);

        return messageLogList;
    }

    @Transactional
    public List<MessageLog> createMessageAllGroup
        (Long messageTypeId, List<Long> exceptGroupIds, LocalDateTime reserveTime, String content) {

        Optional<MessageType> typeInfo = messageTypeRepository.findById(messageTypeId);

        // todo: 추후에 그룹이 많아지면 loop으로 처리
        List<Group> groupInfo = groupService.findGroupListNotIn(exceptGroupIds);

        if (typeInfo.isEmpty() || groupInfo.isEmpty()) {
            return null;
        }

        List<MessageLog> messageLogList = messageLogRepository.saveAll(groupInfo.stream()
            .map(g -> MessageLog.messageLog(typeInfo.get(), g, reserveTime, content)).toList());

        messageUtils.registerTaskSchedule(messageLogList, reserveTime);

        return messageLogList;
    }

    @Override
    public MessageLog findByMessageId(Long id) {
        return messageLogRepository.findById(id).orElse(null);
    }

    @Override
    public Page<MessageLog> findMessageLogList(Pageable pageable) {

        return messageLogRepository.findAll(pageable);
    }

    @Override
    public List<MessageLog> findReservedMessage() {
        return messageLogRepository.findAllByReservedMessage(LocalDateTime.now());
    }
}
