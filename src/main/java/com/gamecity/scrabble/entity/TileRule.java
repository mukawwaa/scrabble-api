package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "TileRule")
@Table(name = "tile_rules")
@NamedQuery(name = "loadAllTileRules", query = "Select tr from TileRule tr where tr.rule.id = :ruleId")
public class TileRule extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = -8275171557545635117L;

    @Column(name = "letter")
    private String letter;

    @Column(name = "piece")
    private Integer piece;

    @Column(name = "score")
    private Integer score;

    @Column(name = "vowel", nullable = false, columnDefinition = "tinyint default 0")
    private boolean vovel;

    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = Rule.class)
    private Rule rule;

    public String getLetter()
    {
        return letter;
    }

    public void setLetter(String letter)
    {
        this.letter = letter;
    }

    public Integer getPiece()
    {
        return piece;
    }

    public void setPiece(Integer piece)
    {
        this.piece = piece;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public boolean isVovel()
    {
        return vovel;
    }

    public void setVovel(boolean vovel)
    {
        this.vovel = vovel;
    }

    public Rule getRule()
    {
        return rule;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
