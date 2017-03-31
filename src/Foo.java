import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.Robot;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Foo extends JFrame{
    private RectPane rp;
    private JMenuBar jmb;
    private JMenu jm1, jm2;
    private JLabel count;
    private Timer timer;
    private boolean end;
    private int c = 0;
    private int d = 0;
    private int topScore = 0;
    private int topScored = 0;
    private File f;
    public Foo() {
        super("数字魔板");
        initGui();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(1);
            }
        });
    }
    private void initGui() {
        f = new File("score.txt");
        if(!f.exists()) {
            try {
                f.createNewFile();
                BufferedWriter w = new BufferedWriter(new FileWriter(f));
                w.write("1000");
                w.newLine();
                w.write("1000");
                topScore = 1000;
                topScored = 1000;
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader r = new BufferedReader(new FileReader(f));
                topScore = Integer.parseInt(r.readLine());
                topScored = Integer.parseInt(r.readLine());
                r.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);
        jmb = new JMenuBar();
        jm1 = new JMenu("难度");
        jm2 = new JMenu("其他");
        count = new JLabel("0秒");
        count.setBounds(0, 500, 800, 80);
        count.setFont(new Font("Sans-serif", 1, 50));
        count.setBackground(Color.WHITE);
        count.setForeground(Color.blue);
        count.setHorizontalAlignment(SwingConstants.CENTER);
        count.setOpaque(true);
        getContentPane().add(count);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rp.col == 3) {
                    count.setText("" + (++c) + "秒");
                }
                if(rp.col == 4) {
                    count.setText("" + (++d) + "秒");
                }
                if(rp.getEnd) {
                    end = rp.getEnd;
                    GameOver();
                }
            }
        });
        timer.start();
        JMenuItem jmi1 = new JMenuItem("初级");
        JMenuItem jmi2 = new JMenuItem("高级");
        JMenuItem jmi3 = new JMenuItem("最高分");
        JMenuItem jmi4 = new JMenuItem("自动");
        jmi1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rp.initRect(250, 150, 300, 300, 3, 3);
                count.setText("0秒");
                c = 0;
                d = 0;
                end = false;
                rp.t.stop();
                timer.start();
            }
        });
        jmi2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rp.initRect(200, 100, 400, 400, 4, 4);
                count.setText("0秒");
                c = 0;
                d = 0;
                end = false;
                rp.t.stop();
                timer.start();
            }
        });
        jmi3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                JOptionPane.showMessageDialog(getParent(), "目前初级最短用时为:" + topScore + "秒\n目前高级最短用时为:" + topScored + "秒", "最高分", JOptionPane.INFORMATION_MESSAGE);
                if(!end) {
                    timer.start();
                }
            }
        });
        jmi4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                rp.Auto();
            }
        });
        jm1.add(jmi1);
        jm1.add(jmi2);
        jm2.add(jmi3);
        jm2.add(jmi4);
        jmb.add(jm1);
        jmb.add(jm2);
        jmb.setBounds(0, 0, 800, 30);
        getContentPane().add(jmb);
        rp = new RectPane(250, 150, 300, 300, 3, 3);
        getContentPane().add(rp);
        setVisible(true);
    }

    class RectPane extends JPanel {
        public boolean getEnd = false;
        public int [][]nums;
        private Rect [][]R;
        private int lin, col;
        public String solve;
        public HashMap<String, Node> hash;
        public HashMap<String, Node> hadget;
        public Comparator<Node> nodeComparator;
        public PriorityQueue<Node> p;
        public Vector<Node> v;
        public Timer t;
        public int solen;
        public RectPane(int x, int y, int w, int h, int lin, int col) {
            initRect(x, y, w, h, lin, col);
            t = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(solen);
                    if(solen == -1) {
                        t.stop();
                        JOptionPane.showMessageDialog(null, "任务完成", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    switch (solve.charAt(solen)) {
                        case 'u':
                            MoveUp();
                            ReFresh();
                            break;
                        case 'd':
                            MoveDown();
                            ReFresh();
                            break;
                        case 'l':
                            MoveLeft();
                            ReFresh();
                            break;
                        case 'r':
                            MoveRight();
                            ReFresh();
                            break;
                    }
                    solen--;
                }
            });
            hash = new HashMap<>();
            hadget = new HashMap<>();
            nodeComparator = new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if(o1.Manhattan < o2.Manhattan) {
                        return 1;
                    }
                    else if(o1.Manhattan > o2.Manhattan) {
                        return -1;
                    }
                    return 0;
                }
            };
            p = new PriorityQueue<>(nodeComparator);
            v = new Vector<>();
        }

        private void initRect(int X, int Y, int w, int h, int lin, int col) {
            removeAll();
            repaint();
            setLocation(X, Y);
            setSize(w, h);
            setLayout(null);
            this.lin = lin;
            this.col = col;
            nums = new int[lin][col];
            RandomNums();
            while (!CanGetEnd()) {
                RandomNums();
            }
            R = new Rect[lin][col];
            for(int x = 0;x < lin;x++) {
                for(int y = 0;y < col;y++) {
                    if(nums[x][y] == 0) {
                        R[x][y] = new Rect("", 100 * y, 100 * x);
                    }
                    else {
                        R[x][y] = new Rect("" + nums[x][y], 100 * y, 100 * x);
                    }
                    R[x][y].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mousePressed(e);
                            Render(e.getComponent().getLocation());
                            getEnd = chkEnd();
                        }
                    });
                    add(R[x][y]);
                }
            }
        }

        public void RandomNums() {
            Vector v = new Vector(lin * col);
            int zerox = -1, zeroy = -1;
            for(int i = 0;i < lin * col;i++) {
                v.add("" + i);
            }
            for(int i = 0;i < lin;i++) {
                for(int j = 0;j < col;j++) {
                    nums[i][j] = Integer.parseInt((String)v.get((int)(Math.random() * 100) % v.size()));
                    v.remove("" + nums[i][j]);
                    if(nums[i][j] == 0) {
                        zerox = i;
                        zeroy = j;
                    }
                }
            }
            nums[zerox][zeroy] = nums[lin - 1][col - 1];
            nums[lin - 1][col - 1] = 0;
        }

        public boolean CanGetEnd() {
            Vector v = new Vector(lin * col);
            int res = 0;
            for(int i = 0;i < lin;i++) {
                for(int j = 0;j < col;j++) {
                    v.add(nums[i][j]);
                }
            }
            for(int i = v.size() - 1;i >= 0;i--) {
                int foo = (int) v.get(i);
                for(int j = i;j >= 0;j--) {
                    if((int) v.get(j) > foo && foo != 0) {
                        res++;
                    }
                }
            }
            if(res % 2 != 0) {
                return false;
            }
            return true;
        }

        public void Render(Point p) {
            int py = p.x / 100;
            int px = p.y / 100;
            int zerox = -1, zeroy = -1;
            for(int x = 0;x < lin;x++) {
                for(int y = 0;y < col;y++) {
                    if(nums[x][y] == 0) {
                        zerox = x;
                        zeroy = y;
                    }
                }
            }
            if((Math.abs(px - zerox) == 1 && Math.abs(py - zeroy) == 0) || (Math.abs(px - zerox) == 0 && Math.abs(py - zeroy) == 1)) {
                nums[zerox][zeroy] = nums[px][py];
                nums[px][py] = 0;
            }
            for(int i = 0;i < lin;i++) {
                for(int j = 0;j < col;j++) {
                    if(nums[i][j] == 0) {
                        R[i][j].setText("");
                    }
                    else {
                        R[i][j].setText("" + nums[i][j]);
                    }
                }
            }
        }

        private boolean chkEnd() {
            int k = 0;
            for(int i = 0;i < lin;i++) {
                for(int j = 0;j < col;j++) {
                    if(nums[i][j] != ((++k) % (lin * col))) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Point getZero() {
            int x = 0, y = 0;
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    if(nums[i][j] == 0) {
                        x = j;
                        y = i;
                    }
                }
            }
            return new Point(x, y);
        }

        public void MoveUp() {
            int x = getZero().x;
            int y = getZero().y;
            if(y > 0) {
                nums[y][x] = nums[y-1][x];
                nums[y-1][x] = 0;
            }
        }

        public void MoveDown() {
            int x = getZero().x;
            int y = getZero().y;
            if(y < col - 1) {
                nums[y][x] = nums[y+1][x];
                nums[y+1][x] = 0;
            }
        }

        public void MoveLeft() {
            int x = getZero().x;
            int y = getZero().y;
            if(x > 0) {
                nums[y][x] = nums[y][x-1];
                nums[y][x-1] = 0;
            }
        }

        public void MoveRight() {
            int x = getZero().x;
            int y = getZero().y;
            if(x < col - 1) {
                nums[y][x] = nums[y][x+1];
                nums[y][x+1] = 0;
            }
        }

        public void ReFresh() {
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    if(nums[i][j] == 0) {
                        R[i][j].setText("");
                    }
                    else {
                        R[i][j].setText("" + nums[i][j]);
                    }
                }
            }
        }

        public Point getLocal(int[][] n, int m) {
            int x = 0, y = 0;
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    if(n[i][j] == m) {
                        x = j;
                        y = i;
                    }
                }
            }
            return new Point(x, y);
        }

        class Node {
            public char operation;
            public int[][] nms;
            public int Manhattan;
            public String Hashcode;
            public String HadGet;
            public int k;
            public Node(int[][] n, char op, int m) {
                nms = new int[col][lin];
                for(int i = 0;i < col;i++) {
                    for(int j = 0;j < lin;j++) {
                        nms[i][j] = n[i][j];
                    }
                }
                k = m;
                Hashcode = "";
                HadGet = "";
                operation = op;
                Manhattan = 0;
                setManhattan();
                setHashcode();
            }

            public void setManhattan() {
                int k = 1;
                for(int i = 0;i < col;i++) {
                    for(int j = 0;j < lin;j++) {
                        if(k == lin * col) {
                            return;
                        }
                        int x = getLocal(nms, k).x;
                        int y = getLocal(nms, k++).y;
                        Manhattan += Math.abs(x - j);
                        Manhattan += Math.abs(y - i);
                    }
                }
                Manhattan *= 10;
                Manhattan += k;
            }

            public void setHashcode() {
                for(int i = 0;i < col;i++) {
                    for(int j = 0;j < lin;j++) {
                        Hashcode += String.valueOf(nms[i][j]);
                        HadGet += String.valueOf(nms[i][j]);
                    }
                }
                Hashcode += operation;
            }

        }

        public int[][] expand(int[][] a, char opreation) {
            int[][] n = new int[col][lin];
            int x = 0;
            int y = 0;
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    if(a[i][j] == 0) {
                        x = j;
                        y = i;
                    }
                    n[i][j] = a[i][j];
                }
            }
            switch (opreation) {
                case 'u':
                    if(y > 0) {
                        n[y][x] = n[y-1][x];
                        n[y-1][x] = 0;
                    }
                    break;
                case 'd':
                    if(y < col - 1) {
                        n[y][x] = n[y+1][x];
                        n[y+1][x] = 0;
                    }
                    break;
                case 'l':
                    if(x > 0) {
                        n[y][x] = n[y][x-1];
                        n[y][x-1] = 0;
                    }
                    break;
                case 'r':
                    if(x < col - 1) {
                        n[y][x] = n[y][x+1];
                        n[y][x+1] = 0;
                    }
                    break;
            }
            return n;
        }

        public boolean isEnd(int[][] a) {
            int k = 1;
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    if(a[i][j] != ((k++) % (lin * col))) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean canUp(int[][] n) {
            Point p = getLocal(n, 0);
            if(p.y <= col - 1 && p.y > 0) {
                return true;
            }
            return false;
        }

        public boolean canDown(int[][] n) {
            Point p = getLocal(n, 0);
            if(p.y >= 0 && p.y < col - 1) {
                return true;
            }
            return false;
        }

        public boolean canLeft(int[][] n) {
            Point p = getLocal(n, 0);
            if(p.x > 0 && p.x <= lin - 1) {
                return true;
            }
            return false;
        }

        public boolean canRight(int[][] n) {
            Point p = getLocal(n, 0);
            if(p.x >= 0 && p.x < lin - 1) {
                return true;
            }
            return false;
        }

        public void Auto() {
            if(lin == 4) {
                JOptionPane.showMessageDialog(this, "功能未实现", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            p.clear();
            v.clear();
            hadget.clear();
            hash.clear();
            Node begin = new Node(nums, 's', 0);
            Node node;
            p.add(begin);
            hash.put(begin.Hashcode, begin);
            hadget.put(begin.HadGet, begin);
            int k = 0;
            while (true) {
                k++;
                int n = p.size();
                System.out.println(n);
                for(int i = 0;i < n;i++) {
                    node = p.poll();
                    hash.put(node.Hashcode, node);
                    hadget.put(node.HadGet, node);
                    v.add(node);
                    if(isEnd(node.nms)) {
                        getSolve(node);
                        return;
                    }
                }
                for(int i = 0;i < v.size();i++) {
                    if(canDown(v.get(i).nms)) {
                        Node foo = new Node(expand(v.get(i).nms, 'd'), 'd', k);
                        if(hadget.containsKey(foo.HadGet));
                        else {
                            p.add(foo);
                        }
                    }
                    if(canUp(v.get(i).nms)) {
                        Node foo = new Node(expand(v.get(i).nms, 'u'), 'u', k);
                        if(hadget.containsKey(foo.HadGet));
                        else {
                            p.add(foo);
                        }
                    }
                    if(canLeft(v.get(i).nms)) {
                        Node foo = new Node(expand(v.get(i).nms, 'l'), 'l', k);
                        if(hadget.containsKey(foo.HadGet));
                        else {
                            p.add(foo);
                        }
                    }
                    if(canRight(v.get(i).nms)) {
                        Node foo = new Node(expand(v.get(i).nms, 'r'), 'r', k);
                        if(hadget.containsKey(foo.HadGet));
                        else {
                            p.add(foo);
                        }
                    }
                }
                v.removeAllElements();
            }
        }

        public void getSolve(Node n) {
            solve = "";
            char op = n.operation;
            solve += op;
            int[][] foo;
            String bar;
            while (op != 's') {
                switch (op) {
                    case 'u':
                        foo = expand(n.nms, 'd');
                        bar = "";
                        for(int i = 0;i < lin;i++) {
                            for(int j = 0;j < col;j++) {
                                bar += ("" + foo[i][j]);
                            }
                        }
                        n = hadget.get(bar);
                        op = n.operation;
                        solve += op;
                        break;
                    case 'd':
                        foo = expand(n.nms, 'u');
                        bar = "";
                        for(int i = 0;i < lin;i++) {
                            for(int j = 0;j < col;j++) {
                                bar += ("" + foo[i][j]);
                            }
                        }
                        n = hadget.get(bar);
                        op = n.operation;
                        solve += op;
                        break;
                    case 'l':
                        foo = expand(n.nms, 'r');
                        bar = "";
                        for(int i = 0;i < lin;i++) {
                            for(int j = 0;j < col;j++) {
                                bar += ("" + foo[i][j]);
                            }
                        }
                        n = hadget.get(bar);
                        op = n.operation;
                        solve += op;
                        break;
                    case 'r':
                        foo = expand(n.nms, 'l');
                        bar = "";
                        for(int i = 0;i < lin;i++) {
                            for(int j = 0;j < col;j++) {
                                bar += ("" + foo[i][j]);
                            }
                        }
                        n = hadget.get(bar);
                        op = n.operation;
                        solve += op;
                        break;
                }
            }
            System.out.println(solve);
            solen = solve.length() - 2;
            if(solen < 0) {
                return;
            }
            for(int i = 0;i < col;i++) {
                for(int j = 0;j < lin;j++) {
                    R[i][j].removeMouseListener(R[i][j].getMouseListeners()[0]);
                }
            }
            t.start();

        }
    }

    class Rect extends JLabel {
        public Rect(String text, int x, int y) {
            super(text);
            setFont(new Font("Sans-serif", 1, 50));
            setSize(98, 98);
            setLocation(x, y);
            setBorder(BorderFactory.createLineBorder(Color.PINK, 1));
            setBackground(Color.cyan);
            setForeground(Color.blue);
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }
    }

    public void GameOver() {
        rp.getEnd = false;
        timer.stop();
        if(c < topScore && c > 0) {
            switch (JOptionPane.showConfirmDialog(null, "您已刷新纪录，是否保存分数", "保存", JOptionPane.YES_NO_OPTION)) {
                case 0:
                    topScore = c;
                    Save();
                    break;
                case 1:
                    break;
            }
        }
        else if(d < topScored && d > 0) {
            switch (JOptionPane.showConfirmDialog(null, "您已刷新纪录，是否保存分数", "保存", JOptionPane.YES_NO_OPTION)) {
                case 0:
                    topScored = d;
                    Save();
                    break;
                case 1:
                    break;
            }
        }
        else {
            if(rp.col == 3) {
                JOptionPane.showMessageDialog(getParent(), "游戏结束,用时" + c + "秒", "消息", JOptionPane.INFORMATION_MESSAGE);
            }
            if(rp.lin == 4) {
                JOptionPane.showMessageDialog(getParent(), "游戏结束,用时" + d + "秒", "消息", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        rp.removeAll();
    }

    public void Save() {
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(f));
            w.write(new String(String.valueOf(topScore)));
            w.newLine();
            w.write(new String(String.valueOf(topScored)));
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // write your code here
        new Foo();
    }
}