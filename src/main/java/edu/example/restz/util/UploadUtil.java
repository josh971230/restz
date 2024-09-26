package edu.example.restz.util;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class UploadUtil {   //실제 업로드 수행
    @Value("${edu.example.upload.path}")  //application.properties 파일에서 업로드 설정 경로 읽어오기
    private String uploadPath;

    @PostConstruct  //객체 생성 후 자동 실행 메서드 지정
    public void init() {    //업로드 경로 실제 존재 여부 확인 및 처리
        File tempDir = new File(uploadPath);

        if( !tempDir.exists() ) {   //업로드 경로가 존재하지 않으면
            log.info("--- tempDir : " + tempDir);
            tempDir.mkdir();        //업로드 디렉토리 생성
        }

        uploadPath = tempDir.getAbsolutePath();
        log.info("--- getPath() : " + tempDir.getPath());
        log.info("--- uploadPath : " + uploadPath);
        log.info("-------------------------------------");
    }

    //0.업로드 수행 - FileController에서 업로드 파일 확장자 체크가 끝난 뒤
    public List<String> upload(MultipartFile[] files) {  //1.컨트롤러에 전달된 업로드 파일들을 매개변수로 받기
        List<String> filenames = new ArrayList<>();
        for(MultipartFile file : files){
            if( !file.getContentType().startsWith("image") ) { //2.전달받은 파일이 image 타입이 아닌 경우
                log.error("--- 지원하지 않는 파일 타입 : " + file.getContentType());  //"--- 지원하지 않는 파일 타입 : ~~~~"를 출력하고
                continue;        //다음 파일 확인
            }

            String uuid = UUID.randomUUID().toString();
            String saveFilename = uuid + "_" + file.getOriginalFilename();  //3.이미지 파일명이 중복되지 않게  파일명에 uuid_ 를 결합하여 저장
            String savePath = uploadPath + File.separator;   //4.업로드 경로에 파일 구분자File.separator를 결합하여 저장

            try {
                file.transferTo(new File(savePath + saveFilename));//---- 실제 파일 업로드 처리 --------

                //썸네일 파일 생성
                Thumbnails.of(new File(savePath + saveFilename))
                        .size(150, 150)
                        .toFile(savePath + "s_" + saveFilename);

                filenames.add(saveFilename);    //5.업로드된 파일명을 List 객체에 저장
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return filenames;   //5.1 저장한 객체 반환
    }   //6.FileController에서는 전달받은 값을 상태코드 200으로 반환

    //업로드 파일 삭제
    public void deleteFile(String filename){   //1.FileController에서 삭제할 파일명을 매개변수로 받기
        File file = new File(uploadPath + File.separator + filename);   //2.삭제할 원본 File 객체 생성   - 저장경로 + 파일 구분자 + 파일명
        File thumbFile = new File(uploadPath + File.separator +
                                  "s_" + filename);                 //3.원본의 썸네일 File 객체 생성  -           "

        try {
            if (file.exists())      file.delete();       //4.만약 원본 파일이 존재하면 파일 삭제-delete() 이용
            if (thumbFile.exists()) thumbFile.delete();  //5.만약 썸네일 파일이 존재하면 파일 삭제-delete() 이용
        } catch (Exception e) {         //6.만약 예외 처리를 해야 하면 try/catch로
            log.error(e.getMessage());  //  발생한 예외 메시지를 에러 로그로 출력
        }
    }
}

