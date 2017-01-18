package novemberizing.rx.example.operators.block;

import novemberizing.rx.Block;
import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;

/**
 * Created by novemberizing on 2017-01-18.
 */
public class Example {

    public static void main(String[] args){
        Operator<String, Integer> stoi = Operator.Op(new novemberizing.rx.Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o) + 1;
            }
        });

        Func<Integer, String> f = new novemberizing.rx.Func<Integer, String>() {
            @Override
            public String call(Integer o) {
                return Integer.toString(o);
            }
        };

        stoi.subscribe(Subscribers.Just("stoi >"));

        Operator<Integer, String> itos = Operator.Op(new novemberizing.rx.Func<Integer, String>() {
            @Override
            public String call(Integer o) {
                return Integer.toString(o) + "1";
            }
        });

        itos.subscribe(Subscribers.Just("stoi >"));


        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end(itos).subscribe(Subscribers.Just("block >"));
            block.exec("1").subscribe(Subscribers.Just("block(task) 1>"));

            itos.exec(1);
        }

        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end(f).subscribe(Subscribers.Just("block >"));
            block.exec("1").subscribe(Subscribers.Just("block(task) 2>"));

            itos.exec(1);
        }
    }
}
