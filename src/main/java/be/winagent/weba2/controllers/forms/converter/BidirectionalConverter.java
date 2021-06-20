package be.winagent.weba2.controllers.forms.converter;

public interface BidirectionalConverter<Target, Source> extends Converter<Target, Source> {
    Source reverseUpdate(Source target, Target source);
    Source reverseBuild(Target source);
}
