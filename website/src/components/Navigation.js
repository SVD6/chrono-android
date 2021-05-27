
const Navigation = (props) => {
  const { setToggle } = props;
  
  return <nav id="navigation">
    <a href="#home" className="nav-link" tabIndex="0" onClick={() => {setToggle(false)}}>Home</a>
    <a href="#features" className="nav-link" tabIndex="0" onClick={() => {setToggle(false)}}>Features</a>
    <a href="#contact" className="nav-link" tabIndex="0" onClick={() => {setToggle(false)}}>Contact Us</a>
  </nav>
};

export default Navigation