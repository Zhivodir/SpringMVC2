package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, byte[]> photos = new HashMap<Long, byte[]>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping("/list_of_photo")
    public String showList(Model model) {
        model.addAttribute("listOfPhotos", photos);
        return "list_of_photo";
    }

    @RequestMapping("/list_for_archive")
    public String showListForArchive(Model model) {
        model.addAttribute("listForArchive", photos);
        return "list_for_archive";
    }

    @RequestMapping(value = "/delete_all_checked", method = RequestMethod.POST)
    public String delAllChecked(Model model, @RequestParam long[] checked_photos) {
        for(Long photo : checked_photos){
            photos.remove(photo);
        }
        return showList(model);
    }

    @RequestMapping(value = "/archive_all_checked", method = RequestMethod.POST)
    public String archiveAllChecked(Model model, @RequestParam long[] checked_photos) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("c:/temp/test.zip");

            ZipOutputStream zout = new ZipOutputStream(fout);
            for (Long id : checked_photos) {
                ZipEntry zipEntry = new ZipEntry(id.toString());
                zipEntry.setSize(photos.get(id).length);
                zout.putNextEntry(zipEntry);
                zout.write(photos.get(id));
                zout.closeEntry();
            }
            zout.close();
        }catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        return "index";
    }
}
