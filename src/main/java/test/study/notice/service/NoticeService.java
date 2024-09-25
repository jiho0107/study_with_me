package test.study.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.notice.domain.Notice;
import test.study.notice.repository.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice save(Notice notice){
        return noticeRepository.save(notice);
    }

    public List<Notice> findAllNotices(){
        return noticeRepository.findAllNotices();
    }

    public Notice findNotice(Long noticeId){
        return noticeRepository.findNotice(noticeId);
    }

    public List<Notice> noticesOfGroup(Long groupId){
        return noticeRepository.noticesOfGroup(groupId);
    }
}
