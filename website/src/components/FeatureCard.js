import react from 'react';
import ReactFreezeFrame from 'react-freezeframe';

function FeatureCard(props){
  return(
    <div className="feature-card">
        <section className="quote-block">
          <h2>{props.title}</h2>
          <p className="quote-block-subtitle sm">{props.subtitle}</p>
        </section>
        <article className="feature-description">
          {/* <ReactFreezeFrame> */}
            <img 
              src={props.jpg}
              onMouseOver={(e)=>e.target.src =`${props.gif}`}
              onMouseOut={(e)=>e.target.src =`${props.jpg}`}
              onFocus={(e)=>e.target.src =`${props.gif}`}
              onBlur={(e)=>e.target.src =`${props.jpg}`}
              alt="feature-gif" 
              className="feature-gif"
              tabIndex="0"/>
          {/* </ReactFreezeFrame> */}
          
        </article>
      </div>
  );
};

export default FeatureCard;