package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyClass {
    public static final String CONTENT_01 = "弱者普遍易怒如虎，而且容易暴怒。强者通常平静如水，并且相对平和。一个内心不强大的人，自然内心不够平静。内心不平静的人，处处是风浪。再小的事，都会被无限放大。一个内心不强大的人，心中永远缺乏安全感。\n" +
            "\n" +
            "如何成为一个内心强大的人？　　不够强大，意味着很容易受到外界的影响，通常表现为：要么特别在意别人的看法，要么活在他人的眼目口舌之中。从而失去独立的判断能力，变得摇摆不定和坐立不安。\n" +
            "\n" +
            "　　要想成为一个内心强大的人，需要具备至少以下六大品质特征：1）高度自律和自黑；2）必须经历绝望；3）培养独处的能力；4）不设限的思考；5）需要一个信仰；6）BE YOURSELF（做自己）。\n" +
            "\n" +
            "　　1。 高度自律和自黑\n" +
            "\n" +
            "　　为什么不说自信呢？不自信的人，普遍内心比较脆弱。一个自信的人，对自己充满信心，做事往往带着积极向上的力量，并时刻充满激情。所有的盲目自信，和空腹自信，都是自以为是。心中要有真才实学，哪怕在不断的试错，但终究能到达攀登高峰的那一天。\n" +
            "\n" +
            "　　人的自信到底从何而来？以及如何培养自己的自信？高度的自信，从高度的自律而来。自律又是什么？自律就是自己管理自己，自己约束自己。这是一个很重要的能力。先学会克制自己，用严格的日程表来控制生活，才能在这种自律中不断磨练出自信。自信也代表着对事情的控制能力，连最基本的时间都控制不了，谈何自信？\n" +
            "\n" +
            "　　除了自律以外，自黑的能力也相当重要。世界之大，什么鸟都有。等你哪一天稍微做出点成绩，很多认识或不认识的人便在背后，唧唧歪歪的议论是非。从最开始的吐槽，到断章取义的论断甚至无趣的黑你。\n" +
            "\n" +
            "　　自黑就是自己嘲笑自己，自己黑色幽默自己。自黑是一种沟通方式，也是一种境界，更是一种另类的修养。自黑不是等到有人说你时才出现，而是从头到尾都需要有的能力。你必须看透那些无聊恶俗的人，要比他们还会擅长黑自己，待他们自知无趣后，便会羞愧的退场而去。\n" +
            "\n" +
            "　　2。 必须经历绝望\n" +
            "\n" +
            "　　经历绝望的意思，就是已经走过这段岁月。也许你还未曾绝望过，并不意味着你不坚强，但一定没有经历过绝望的人坚强。未绝望过的人生是不完美的人生。绝望可能是情感、事业抑或无法面对的孤独等等。\n" +
            "\n" +
            "　　「必须」 是一个前缀词，一个重要的状态。「必须」并非主动选择，而是做好充分的心理准备。当绝望来临时，坦然无惧的接受它，即使当下极其痛苦，甚至失去了自我。在绝望中寻找希望，才是值得体验的一种人生。\n" +
            "\n" +
            "　　强大的人不是征服什么，而是能承受什么。一些事情，只有经历过了，才能明白其中的道理，和懂得人生的真谛。绝望并不可怕，可怕的是失去勇气和激情。经历绝望，但不要被绝望吞噬。相反你要胜过它，如同战胜黑暗，迎接光明一样。\n" +
            "\n" +
            "　　3。 培养独处的能力\n" +
            "\n" +
            "　　孤独和独处并不是一件事，是两码事，而且经常会被混淆。人们往往把交往看作一种能力，却忽略了独处也是一种能力，并且在一定意义上是比交往更为重要的一种能力。如果说不擅交际是一种性格的弱点，那么，不耐孤独就简直是一种灵魂的缺陷了。\n" +
            "\n" +
            "　　要耐得住寂寞，不随波逐流。孤单是一个人的狂欢，狂欢是一群人的孤单。所谓的成熟，就是你越长大，越能学会一个人适应一切。在独处的时光中，找到自己真正热爱的，并培养自己独立的判断能力。\n" +
            "\n" +
            "　　人只有先学会爱自己，才有能力爱他人。如果你不学着与自己对话，便更难和别人交流。越能独处的人，越能面对和理解困境，也越能与他人相处。因为能瞬间换位思考，更能设身处地为对方着想。\n" +
            "\n" +
            "　　4。 不设限的思考\n" +
            "\n" +
            "　　你的眼光要比别人远，你的心胸要比他人宽广。天天算计的人生，未来迟早也会被人生算计。除此以外，还活在各种小心眼和小格局之中。\n" +
            "\n" +
            "　　人生如同开车一样，当你比别人快30码，你体会到的感受别人无法感知。人生又如同开飞机，当你比别人高30000英尺，你看到的视野自然不同于他人。意思就是，当你在追求更高更远的美景时，也就不必在意他人短视的眼目。一切自然云淡风轻，不再受影响。\n" +
            "\n" +
            "　　宽阔之后，就不会受狭隘主义的捆绑。自由之后，就不会受形式主义的限制。\n" +
            "\n" +
            "　　5。 需要一个信仰\n" +
            "\n" +
            "　　人实在太有限，不论你信仰什么，总归要有一个信仰，否则和动物无任何区别。\n" +
            "\n" +
            "　　在这个时代，几乎人人都有信仰，只是各自的信仰不同而已。有人信仰权力，有人信仰金钱，有人信仰自我，有人信仰爱情，有人信仰幸福，有人信仰美食，有人信仰党派，有人信仰制度，有人信仰无神，有人信仰有神，有人信仰多神，有人笃信基督……\n" +
            "\n" +
            "　　不管什么样的信仰，都令人值得尊重，而且任何信仰都需要深入了解并相信，才能称之为信仰。任何一种信仰，如果是稀里糊涂的信，都称不上信仰，属于迷信，包括基督教在内的所有宗教信仰。\n" +
            "\n" +
            "　　智慧和真理，有着天与地的距离。学会渴慕真理胜过追求智慧。真理能使人更有智慧的来看待世间万物，宽容的态度面对错综复杂的人际关系，无比坚定的信念奔走人生之路，从容淡定的直面人性的黑暗和世间的悲剧。\n" +
            "\n" +
            "　　6。 BE YOURSELF（做自己）\n" +
            "\n" +
            "　　JUST BE YOURSELF。 不要试图取悦所有人，现在做不到，未来也做不到。\n" +
            "\n" +
            "　　人不可能面面俱到。每个人应该会有自己最在乎的人，他们才是你生命中最宝贵的财富。倘若他们对你所做的事有误解或质疑，值得你花时间去回应和解释。\n" +
            "\n" +
            "　　以下是个人最在乎的三类群体，仅供参考：1、家人（亲戚不算在内，一年到头只见一回，并不清楚你的人生定位）；2、人生知己（为数不多的真正挚友，一切都知根知底）；3、牧师或导师（属灵上的导师，和职场中的前辈引领者）。\n" +
            "\n" +
            "　　他们的评价/意见/建议，则会认真的聆听。若存在不理解时，一定会充分的解释和回应。只有真正在乎的人相对全面了解你，其他人大部分是片面的认知。在生活中也没有特别的交集，有的只是道听途说或仅仅是几面之缘而已。\n" +
            "\n" +
            "　　不必回应那些熟悉的陌生人，他们并不一定真正关心你，更多的只是好奇宝宝。时间如此宝贵，无暇顾及这些，也不在解释的义务范畴。你的人生并不需要活在他人的言语中，还有很多更重要的事等着去做。\n" +
            "\n" +
            "　　内心强大的人，很少在意他人的看法，包括熟悉的陌生人。就像积极的人很少关注消极的信息，即便看到，也自动瞬间被屏蔽或消化。他们很清楚自己的定位和追求。\n" +
            "\n" +
            "　　遇到了障碍，会想尽一切办法铲除。遇到了挫折，也不轻易放弃倒下。克服了困难，便拥有了力量。解决了问题，便拥有了智慧。走出了黑暗，便拥有了希望。对他们而言，这些仅仅是人生的必经之路。\n" +
            "\n" +
            "　　真正内心强大的人，一定有一颗平静的内心，有一颗温柔的心肠，有一颗智慧的头脑。一定经历过狂风暴雨，体验过高山低谷，也见识过人生百态。惟愿我们在人生的道路上，不论何种境遇都能充满智慧的刚强壮胆，成为内心强大的人。";


    private static int index, uploadCount, requestCount;  // 次数
    private static ExecutorService mExecutor;

    public static void main(String[] args){
        System.out.println("Hello world!!!!");

        final String file = "/Users/ianchang/Downloads/log/Log.txt";
        System.out.println(file);
        final String uploadFilePathName = "log_upload";

        // 查看根据目录
        final File filePath = new File(file);

        mExecutor = Executors.newSingleThreadExecutor();


        // 写入日志，被调用了10000次
        for (int i = 0; i < 10000; i++) {

            mExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    if (filePath.length() >= 1024 * 1024 * 2) {
                        System.out.println("该文件大于2M");
                        requestCount++;
                        // 开始上传
                        uploadFile(file, uploadFilePathName);
                    } else {
                        System.out.println("该文件还可以继续读写");
                    }

                    System.out.println("文件大小："+filePath.length());

                    writeToFile(filePath, CONTENT_01 + "$$$$$$");
                    writeToFile(filePath, "############################");
                    writeToFile(filePath, "第"+(index+1)+"遍,还能继续追加吗？");
                    index++;


                    System.out.println("待上传了"+requestCount+"个文件" + index);
                    System.out.println("上传到服务了"+uploadCount+"个文件");

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        System.out.println(Arrays.toString(filePath.getParentFile().list()));

        // 查看上传目录
        File uploadPath = new File(filePath.getParentFile(), uploadFilePathName);
        System.out.println(Arrays.toString(uploadPath.list()));

    }


    public static void writeToFile(File file, String content){

        if (file == null){
            return;
        }

        System.out.println(file.getParent());

        if (file.exists()){
            System.out.println("该目录存在");
        }else {
            System.out.println("该目录不存在");

            try {

                // 创建目录
                createOrExistsDir(file.getParentFile());

                if(file.createNewFile()){
                    System.out.println("创建文件成功");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
//            PrintWriter printWriter = new PrintWriter(new FileWriter(file, true));
//            printWriter.println("您好，这是一个测试！！！");
//            printWriter.println("Hello, this is a test!!!");
            printWriter.println("******************************************");
            printWriter.println(content);
            printWriter.println("******************************************");
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String path, String uploadFilePathName){
        File file = new File(path);
        System.out.println(file.getParent());

        // 日志文件无法动态设置，只能每个文件大于2M时，再复制出来
        // 上传文件目录
        File mUploadFileDir = new File(file.getParent(), uploadFilePathName); // 上传日志文件目录

        System.out.println(mUploadFileDir.getAbsolutePath());

        createOrExistsDir(mUploadFileDir);

        // 上传文件
        File uploadFile = new File(mUploadFileDir, "/LOG_"+formatDate(System.currentTimeMillis())+".txt");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(uploadFile, true);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = fis.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }

            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                if (fis != null){
                    fis.close();
                }

                if (fos != null){
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 删除原日志文件
        file.delete();
        // 再创建日志文件
        createOrExistsFile(file);

        // 开始上传目录
        File[] files = mUploadFileDir.listFiles();
        if (files != null && files.length > 0){
            System.out.println("待上传文件个数："+files.length);
            requestUploadFile(files);
        }
//        requestUploadFile(uploadFile);
    }

    public static boolean createOrExistsDir(File file){
        if (file == null){
            return false;
        }

        if (file.exists()){
            return file.isDirectory();
        }else {
            return file.mkdirs();
        }
    }

    public static boolean createOrExistsFile(File file){
        if (file == null){
            return false;
        }

        if (file.exists()){
            return file.isFile();
        }else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // 模拟网络请求，上传文件
    public static void requestUploadFile(File ... files){

        ArrayList<File> uploadFiles = new ArrayList<>();

        File uploadPath;
        for (File file: files) {

            if (!file.exists()){
                continue;
            }

            uploadCount++;

            // 上传过去的目录
            uploadPath = new File(file.getParentFile().getParent(), "tmp");
            createOrExistsDir(uploadPath);

            // 上传文件
            File uploadFile = new File(uploadPath, file.getName());
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(file);
                fos = new FileOutputStream(uploadFile, true);
                byte[] buffer = new byte[1024];
                int len;

                while ((len = fis.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }

                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

                try {
                    if (fis != null){
                        fis.close();
                    }

                    if (fos != null){
                        fos.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // 上传成功
            uploadFiles.add(file);
        }


        for (File file: uploadFiles) {
            // 删除原日志文件
            file.delete();
        }

    }

    public static String formatDate(long time){
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault());
        return mDateFormat.format(new Date(time));
    }

}
