/*
 *
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.netflix.stats.distribution;


/**
 * Accumulator of statistics about a distribution of
 * observed values that are produced incrementally.
 * <p>
 * Note that the implementation is <em>not</em> synchronized,
 * and simultaneous updates may produce incorrect results.
 * In most cases these incorrect results will be unimportant,
 * but applications that care should synchronize carefully
 * to ensure consistent results.
 * <p>
 * Note that this implements {@link DistributionMBean} and so can be
 * registered as an MBean and accessed via JMX if desired.
 *
 * @author netflixoss $
 * @version $Revision: $
 * FIXME 分配;分布;分发;分送;(商品)运销，经销，分销
 * FIXME 方差，标准方差的计算类；
 */
public class Distribution implements DistributionMBean, DataCollector {

    /*个数*/
    private long numValues;
    private double sumValues;
    /*FIXME todo 为何要平方值，这个数据应该很大，存在越界吗？*/
    private double sumSquareValues;
    private double minValue;
    private double maxValue;

    /*
     * Constructors
     */

    /**
     * Creates a new initially empty Distribution.
     */
    public Distribution() {
        numValues = 0L;
        sumValues = 0.0;
        sumSquareValues = 0.0;
        minValue = 0.0;
        maxValue = 0.0;
    }

    /*
     * Accumulating new values
     *FIXME  累加一个数据 没有线程安全，该类不涉及到共享数据？
     *
     */
    public void noteValue(double val) {
        numValues++;
        sumValues += val;
        sumSquareValues += val * val;
        if (numValues == 1) {
            minValue = val;
            maxValue = val;
        } else if (val < minValue) {
            minValue = val;
        } else if (val > maxValue) {
            maxValue = val;
        }
    }

    /**
     *
     */
    public void clear() {
        numValues = 0L;
        sumValues = 0.0;
        sumSquareValues = 0.0;
        minValue = 0.0;
        maxValue = 0.0;
    }

    /*
     * Accessors
     */

    /**
     * {@inheritDoc}
     */
    public long getNumValues() {
        return numValues;
    }

    /**
     * {@inheritDoc}
     * 平均值
     */
    public double getMean() {
        if (numValues < 1) {
            return 0.0;
        } else {
            return sumValues / numValues;
        }
    }

    /**
     * {@inheritDoc}
     * 方差   todo 方差为何是下面的算法？？
     * 1、方差是各个数据分别与其平均数之差的平方的和的平均数，用字母D表示。在概率论和数理统计中，
     * FIXME 方差（Variance）用来度量随机变量和其数学期望（即均值）之间的偏离程度。
     * FIXME 在许多实际问题中，研究随机变量和均值之间的偏离程度有着重要意义。
     * <p>
     * 其中，x表示样本的平均数，n表示样本的数量，xi表示个体，而s^2就表示方差
     * <p>
     * FIXME 公式 ： 方差=[(x1-x)*(x1-x) + (x2-x)*(x2-x)....]/n
     * <p>
     * FIXME (x1-x)*(x1-x) = x1^2 + x^2 -2x1x
     * FIXME (x2-x)*(x2-x) = x2^2 + x^2 -2x2x
     * FIXME x1^2 + x^2 -2x1x +x2^2 + x^2 -2x2x
     * FIXME x1^2 +x2^2 + x^2 -2x^2  + x^2 -2x^2
     * FIXME x1^2 +x2^2  -2x^2 /2
     * FIXME (x1^ +x2^)2  -x^2
     * FIXME =(sumSquareValues / numValues) - mean * mean;近似值
     * <p>
     * FIXME x1为一个随机数，x为平均数；n为总数据；
     */
    public double getVariance() {
        if (numValues < 2) {
            return 0.0;
        } else if (sumValues == 0.0) {
            return 0.0;
        } else {
            double mean = getMean();
            return (sumSquareValues / numValues) - mean * mean;
        }
    }

    /**
     * FIXME 标准差就是将方差开方得到.
     * FIXME 标准差（Standard Deviation） ，中文环境中又常称均方差，但不同于均方误差（mean squared error，均方误差是各数据偏离真实值的距离平方的平均数，
     * FIXME 也即误差平方和的平均数，计算公式形式上接近方差，它的开方叫均方根误差，均方根误差才和标准差形式上接近），
     * FIXME 标准差是离均差平方和平均后的方根，用σ表示。假设有一组数值X1,X2,X3,......XN（皆为实数），其平均值（算术平均值）为μ，公式如图。
     *
     *
     * FIXME 标准差是 反应多组数据之间稳定值差异的，与样本多少没有关系，有多少样本就反应多少样本之间的数值的稳定性。
     */
    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    /**
     * {@inheritDoc}
     */
    public double getMinimum() {
        return minValue;
    }

    /**
     * {@inheritDoc}
     */
    public double getMaximum() {
        return maxValue;
    }

    /**
     * Add another {@link Distribution}'s values to this one.
     *
     * @param anotherDistribution the other {@link Distribution} instance
     */
    public void add(Distribution anotherDistribution) {
        if (anotherDistribution != null) {
            numValues += anotherDistribution.numValues;
            sumValues += anotherDistribution.sumValues;
            sumSquareValues += anotherDistribution.sumSquareValues;
            minValue = (minValue < anotherDistribution.minValue) ? minValue
                    : anotherDistribution.minValue;
            maxValue = (maxValue > anotherDistribution.maxValue) ? maxValue
                    : anotherDistribution.maxValue;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{Distribution:")
                .append("N=").append(getNumValues())
                .append(": ").append(getMinimum())
                .append("..").append(getMean())
                .append("..").append(getMaximum())
                .append("}")
                .toString();
    }

}
