package ru.nerv.coin.implement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import com.googlecode.fannj.TrainingAlgorithm;

import ru.nerv.coin.*;
	

public interface NervStart {

	default void nervTrain(String pathDataTrane){
        List<Layer> layerList = new ArrayList<Layer>();
        layerList.add(Layer.create(16, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(16, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(8, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        Fann fann = new Fann(layerList);
        //Создаем тренера и определяем алгоритм обучения
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
        trainer.train(new File(pathDataTrane + ".data").getAbsolutePath(), 100000, 100, 0.0001f);
        fann.save(pathDataTrane);
	};
	public void nervConfig();
	public List<DataExchange> getDataExchangePire(String pire);
	public void balance(List<DataExchange> data, List<DataExchange> dataBtc);
	public void saveDataTrainToFile(List<DataExchange> data, List<DataExchange> dataBtc, String pathDataTrane);
}
