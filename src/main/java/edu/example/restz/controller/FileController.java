package edu.example.restz.controller;
import edu.example.restz.exception.UploadNotSupportedException;
import edu.example.restz.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/files")
public class FileController {
    private final UploadUtil uploadUtil;

    @DeleteMapping("/{filename}")   //Delete 요청의 경로에 파라미터값으로 삭제하려는 파일명을 전달받아서
    public ResponseEntity<?> fileDelete(@PathVariable String filename) {
        log.info("--- fileDelete() : " + filename);
        uploadUtil.deleteFile(filename);    //UploadUtil 클래스의 해당 메서드에 전달한 후
        return ResponseEntity.ok().build(); //처리된 결과는 임의로 지정
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(
            @RequestParam("files")MultipartFile[] files){
        log.info("--- uploadFile() : " + files);

        //업로드 파일이 없는 경우 ------
        if( files[0].isEmpty() ) {  //files.length == 0
            throw new UploadNotSupportedException("업로드 파일이 없습니다.");   //UploadNotSupportedException 예외 발생 시키기 - 메시지 : 업로드 파일이 없습니다.
        }

        for(MultipartFile file : files){
            log.info("------------------------------");
            log.info("name : " + file.getName());
            log.info("origin name : " + file.getOriginalFilename());
            log.info("type : " + file.getContentType());

            checkFileExt(file.getOriginalFilename());   //1.확장자 체크 메서드 호출 ---
        }
        return ResponseEntity.ok(uploadUtil.upload(files));
    }

    //업로드 파일 확장자 체크
    public void checkFileExt(String filename) throws UploadNotSupportedException {
        String ext = filename.substring(filename.lastIndexOf(".") + 1 );    //2.이미지 파일 확장자
        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)";

        if(!ext.matches(regExp)) { //3.업로드 파일의 확장자가 위에 해당하지 않는 경우
            //4.UploadNotSupportedException 예외를 발생 시켜서
            //메시지 - 지원하지 않는 형식입니다. : 확장자
            throw new UploadNotSupportedException("지원하지 않는 파일 형식 : " + ext);
            //5.발생한 예외는 호출한 쪽에서 처리하도록 지정
        }
    }
}










